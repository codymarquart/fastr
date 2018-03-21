/*
 * Copyright (c) 2013, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.builtin.base;

import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.singleElement;
import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.stringValue;
import static com.oracle.truffle.r.runtime.RDispatch.INTERNAL_GENERIC;
import static com.oracle.truffle.r.runtime.builtins.RBehavior.COMPLEX;
import static com.oracle.truffle.r.runtime.builtins.RBuiltinKind.INTERNAL;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.r.nodes.attributes.SpecialAttributesFunctions.RemoveNamesAttributeNode;
import com.oracle.truffle.r.nodes.attributes.UnaryCopyAttributesNode;
import com.oracle.truffle.r.nodes.builtin.RBuiltinNode;
import com.oracle.truffle.r.nodes.builtin.base.AsVectorNodeGen.AsVectorInternalNodeGen;
import com.oracle.truffle.r.nodes.builtin.base.AsVectorNodeGen.AsVectorInternalNodeGen.CastPairListNodeGen;
import com.oracle.truffle.r.nodes.builtin.base.AsVectorNodeGen.AsVectorInternalNodeGen.DropAttributesNodeGen;
import com.oracle.truffle.r.nodes.function.CallMatcherNode;
import com.oracle.truffle.r.nodes.function.ClassHierarchyNode;
import com.oracle.truffle.r.nodes.function.ClassHierarchyNodeGen;
import com.oracle.truffle.r.nodes.function.S3FunctionLookupNode;
import com.oracle.truffle.r.nodes.function.S3FunctionLookupNode.Result;
import com.oracle.truffle.r.nodes.objects.GetS4DataSlot;
import com.oracle.truffle.r.nodes.unary.CastComplexNode;
import com.oracle.truffle.r.nodes.unary.CastDoubleNode;
import com.oracle.truffle.r.nodes.unary.CastExpressionNode;
import com.oracle.truffle.r.nodes.unary.CastIntegerNode;
import com.oracle.truffle.r.nodes.unary.CastListNodeGen;
import com.oracle.truffle.r.nodes.unary.CastLogicalNode;
import com.oracle.truffle.r.nodes.unary.CastNode;
import com.oracle.truffle.r.nodes.unary.CastRawNode;
import com.oracle.truffle.r.nodes.unary.CastStringNode;
import com.oracle.truffle.r.nodes.unary.CastSymbolNode;
import com.oracle.truffle.r.runtime.ArgumentsSignature;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RError.Message;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.builtins.RBuiltin;
import com.oracle.truffle.r.runtime.data.RAttributesLayout;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RExpression;
import com.oracle.truffle.r.runtime.data.RPairList;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.RS4Object;
import com.oracle.truffle.r.runtime.data.RSharingAttributeStorage;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.RSymbol;
import com.oracle.truffle.r.runtime.data.RTypedValue;
import com.oracle.truffle.r.runtime.data.model.RAbstractAtomicVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.model.RAbstractListVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.env.REnvironment;
import com.oracle.truffle.r.runtime.interop.ForeignArray2R;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;

@RBuiltin(name = "as.vector", kind = INTERNAL, parameterNames = {"x", "mode"}, dispatch = INTERNAL_GENERIC, behavior = COMPLEX)
public abstract class AsVector extends RBuiltinNode.Arg2 {

    @Child private AsVectorInternal internal = AsVectorInternalNodeGen.create();
    @Child private ClassHierarchyNode classHierarchy = ClassHierarchyNodeGen.create(false, false);

    @Child private S3FunctionLookupNode lookup;
    @Child private CallMatcherNode callMatcher;

    private final ConditionProfile hasClassProfile = ConditionProfile.createBinaryProfile();

    static {
        Casts casts = new Casts(AsVector.class);
        casts.arg("mode").mustBe(stringValue()).asStringVector().mustBe(singleElement()).findFirst();
    }

    protected static AsVectorInternal createInternal() {
        return AsVectorInternalNodeGen.create();
    }

    private static final ArgumentsSignature SIGNATURE = ArgumentsSignature.get("x", "mode");

    @Specialization
    protected Object asVector(VirtualFrame frame, Object x, String mode) {
        // TODO given dispatch = INTERNAL_GENERIC, this should not be necessary
        // However, INTERNAL_GENERIC is not handled for INTERNAL builtins
        RStringVector clazz = classHierarchy.execute(x);
        if (hasClassProfile.profile(clazz != null)) {
            // Note: this dispatch takes care of factor, because there is as.vector.factor
            // specialization in R
            if (lookup == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                lookup = insert(S3FunctionLookupNode.create(false, false));
            }
            Result lookupResult = lookup.execute(frame, "as.vector", clazz, null, frame.materialize(), null);
            if (lookupResult != null) {
                if (callMatcher == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    callMatcher = insert(CallMatcherNode.create(false));
                }
                return callMatcher.execute(frame, SIGNATURE, new Object[]{x, mode}, lookupResult.function, lookupResult.targetFunctionName, lookupResult.createS3Args(frame));
            }
        }
        return internal.execute(x, mode);
    }

    @ImportStatic(RRuntime.class)
    public abstract static class AsVectorInternal extends RBaseNode {

        public abstract Object execute(Object x, String mode);

        @Child private GetS4DataSlot getS4DataSlotNode;
        private final ConditionProfile indirectMatchProfile = ConditionProfile.createBinaryProfile();

        protected static CastNode createCast(RType type) {
            if (type != null) {
                switch (type) {
                    case Any:
                        return null;
                    case Character:
                        return CastStringNode.createNonPreserving();
                    case Complex:
                        return CastComplexNode.createNonPreserving();
                    case Double:
                        return CastDoubleNode.createNonPreserving();
                    case Expression:
                        return CastExpressionNode.createNonPreserving();
                    case Function:
                    case Closure:
                        throw RInternalError.unimplemented("as.vector cast to 'function'");
                    case Integer:
                        return CastIntegerNode.createNonPreserving();
                    case List:
                        return CastListNodeGen.create(true, false, false);
                    case Logical:
                        return CastLogicalNode.createNonPreserving();
                    case PairList:
                        return CastPairListNodeGen.create();
                    case Raw:
                        return CastRawNode.createNonPreserving();
                    case Symbol:
                        return CastSymbolNode.createNonPreserving();
                }
            }
            throw RError.error(RError.SHOW_CALLER, Message.INVALID_ARGUMENT, "mode");
        }

        protected boolean matchesMode(String mode, String cachedMode) {
            return mode == cachedMode || indirectMatchProfile.profile(cachedMode.equals(mode));
        }

        @TruffleBoundary
        @Specialization
        protected Object asVector(@SuppressWarnings("unused") REnvironment x, String mode) {
            RType type = RType.fromMode(mode);
            throw RError.error(RError.SHOW_CALLER, Message.CANNOT_COERCE, RType.Environment.getName(), type != null ? type.getName() : mode);
        }

        // there should never be more than ~12 specializations
        @SuppressWarnings("unused")
        @Specialization(limit = "99", guards = {"!isREnvironment(x)", "matchesMode(mode, cachedMode)"})
        protected Object asVector(Object x, String mode,
                        @Cached("mode") String cachedMode,
                        @Cached("fromMode(cachedMode)") RType type,
                        @Cached("createCast(type)") CastNode cast,
                        @Cached("create(type)") DropAttributesNode drop,
                        @Cached("create()") ForeignArray2R foreignArray2R) {
            if (RRuntime.isForeignObject(x)) {
                Object o = foreignArray2R.convert(x);
                if (!RRuntime.isForeignObject(o)) {
                    return cast == null ? o : cast.doCast(o);
                }
                throw RError.error(RError.SHOW_CALLER, RError.Message.CANNOT_COERCE_EXTERNAL_OBJECT_TO_VECTOR, type == RType.List ? "list" : "vector");
            }
            Object result = x;
            if (x instanceof RS4Object) {
                result = getS4DataSlot((RS4Object) x);
            }
            return drop.execute(result, cast == null ? x : cast.doCast(result));
        }

        private Object getS4DataSlot(RS4Object o) {
            if (getS4DataSlotNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                getS4DataSlotNode = insert(GetS4DataSlot.create(RType.Any));
            }
            return getS4DataSlotNode.executeObject(o);
        }

        public abstract static class DropAttributesNode extends RBaseNode {

            private final RType targetType;

            protected DropAttributesNode(RType targetType) {
                this.targetType = targetType;
            }

            public abstract Object execute(Object original, Object o);

            public static DropAttributesNode create(RType targetType) {
                return DropAttributesNodeGen.create(targetType);
            }

            protected static boolean hasAttributes(Class<? extends RAbstractAtomicVector> clazz, RAbstractAtomicVector o) {
                return clazz.cast(o).getAttributes() != null;
            }

            @Specialization(guards = "o.getAttributes() == null")
            protected static RSharingAttributeStorage drop(@SuppressWarnings("unused") Object original, RSharingAttributeStorage o) {
                // quickly reject any RSharingAttributeStorage without attributes
                return o;
            }

            @Specialization(guards = {"o.getClass() == oClass", "o.getAttributes() != null"})
            protected RAbstractVector dropCached(@SuppressWarnings("unused") Object original, RAbstractAtomicVector o,
                            @Cached("o.getClass()") Class<? extends RAbstractAtomicVector> oClass,
                            @Cached("createBinaryProfile()") ConditionProfile profile) {
                return profile.profile(hasAttributes(oClass, o)) ? oClass.cast(o).copyDropAttributes() : o;
            }

            @Specialization(replaces = "dropCached", guards = "o.getAttributes() != null")
            protected RAbstractVector drop(@SuppressWarnings("unused") Object original, RAbstractAtomicVector o,
                            @Cached("createBinaryProfile()") ConditionProfile profile) {
                return profile.profile(o.getAttributes() != null) ? o.copyDropAttributes() : o;
            }

            @Specialization(guards = {"o.isLanguage()", "o.getAttributes() != null"})
            protected RPairList drop(@SuppressWarnings("unused") Object original, RPairList o) {
                switch (targetType) {
                    case Any:
                    case PairList:
                    case List:
                        return o;
                }
                return RDataFactory.createLanguage(o.getClosure());
            }

            @Specialization(guards = "o.getAttributes() != null")
            protected static RSymbol drop(Object original, RSymbol o) {
                return original == o ? o : RDataFactory.createSymbol(o.getName());
            }

            @Specialization(guards = "pairList.getAttributes() != null")
            protected Object dropPairList(@SuppressWarnings("unused") Object original, RPairList pairList) {
                // dropping already done in the cast node CastPairListNode below
                return pairList;
            }

            @Specialization(guards = "list.getAttributes() != null")
            protected Object drop(Object original, RAbstractListVector list,
                            @Cached("create()") UnaryCopyAttributesNode copyAttributesNode,
                            @Cached("createBinaryProfile()") ConditionProfile originalIsAtomic) {
                if (originalIsAtomic.profile(getRType(original).isAtomic())) {
                    return list;
                }
                if (original instanceof RAbstractVector) {
                    copyAttributesNode.execute(list, (RAbstractVector) original);
                }
                return list;
            }

            @Fallback
            protected Object drop(@SuppressWarnings("unused") Object original, Object o) {
                // includes RExpression, RSymbol
                return o;
            }

            private static RType getRType(Object original) {
                return original instanceof RTypedValue ? ((RTypedValue) original).getRType() : RType.Any;
            }
        }

        // NOTE: this cast takes care of attributes dropping too. Names are never dropped, and other
        // attrs copied only in the case of list, pairlist and expressions (all of them are
        // RAbstractListVector).
        protected abstract static class CastPairListNode extends CastNode {

            @Specialization
            protected Object castPairlist(RExpression x,
                            @Cached("create()") RemoveNamesAttributeNode removeNamesAttributeNode) {
                return fromVectorWithAttributes(x, removeNamesAttributeNode);
            }

            @Specialization
            protected Object castPairlist(RAbstractListVector x,
                            @Cached("create()") RemoveNamesAttributeNode removeNamesAttributeNode) {
                return fromVectorWithAttributes(x, removeNamesAttributeNode);
            }

            @Specialization
            protected Object castPairlist(RAbstractAtomicVector x) {
                return x.getLength() == 0 ? RNull.instance : fromVector(x);
            }

            @Specialization
            protected Object doRNull(@SuppressWarnings("unused") RNull value) {
                return RNull.instance;
            }

            @Specialization
            protected Object doRLanguage(RPairList pairlist) {
                return pairlist;
            }

            @Fallback
            protected Object castPairlist(Object x) {
                String name = x instanceof RTypedValue ? ((RTypedValue) x).getRType().getName() : x.getClass().getSimpleName();
                throw error(Message.CANNOT_COERCE, name, RType.PairList.getName());
            }

            @TruffleBoundary
            private static Object fromVectorWithAttributes(RAbstractContainer x, RemoveNamesAttributeNode removeNamesAttributeNode) {
                if (x.getLength() == 0) {
                    return RNull.instance;
                } else {
                    Object list = fromVector(x);
                    DynamicObject attributes = x.getAttributes();
                    if (attributes != null) {
                        ((RPairList) list).initAttributes(RAttributesLayout.copy(attributes));
                        // names are part of the list already
                        removeNamesAttributeNode.execute(list);
                    }
                    return list;
                }
            }

            @TruffleBoundary
            private static RPairList fromVector(RAbstractContainer x) {
                Object list = RNull.instance;
                RStringVector names = x.getNames();
                for (int i = x.getLength() - 1; i >= 0; i--) {
                    Object name = names == null ? RNull.instance : RDataFactory.createSymbolInterned(names.getDataAt(i));
                    Object data = x.getDataAtAsObject(i);
                    list = RDataFactory.createPairList(data, list, name);
                }
                return (RPairList) list;
            }
        }
    }
}

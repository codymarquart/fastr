/*
 * Copyright (c) 2019, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.runtime.data.model;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.RDoubleVector;
import com.oracle.truffle.r.runtime.data.RRawVector;

/**
 * Provides interop messages for numeric vectors. <br>
 * <ul>
 * <li>{@link RIntVector}</li>
 * <li>{@link RDoubleVector}</li>
 * <li>{@link RRawVector}</li>
 * </ul>
 * 
 */
@ExportLibrary(InteropLibrary.class)
public abstract class RAbstractNumericVector extends RAbstractAtomicVector {

    public RAbstractNumericVector(boolean complete) {
        super(complete);
    }

    protected abstract boolean isScalarNA();

    protected Object getScalarValue() {
        assert getLength() == 1;
        return getDataAtAsObject(0);
    }

    @ExportMessage
    public final boolean isNull(@Cached.Exclusive @Cached("createBinaryProfile()") ConditionProfile isScalar) {
        if (!isScalar.profile(isScalar())) {
            return false;
        }
        return isScalarNA();
    }

    @ExportMessage
    public final boolean isNumber() {
        if (!isScalar()) {
            return false;
        }
        return !isScalarNA();
    }

    @ExportMessage
    public final boolean fitsInByte() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInByte(value);
    }

    @ExportMessage
    public final byte asByte(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asByte(value);
    }

    @ExportMessage
    public final boolean fitsInShort() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInShort(value);
    }

    @ExportMessage
    public final short asShort(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asShort(value);
    }

    @ExportMessage
    public final boolean fitsInInt() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInInt(value);
    }

    @ExportMessage
    public final int asInt(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asInt(value);
    }

    @ExportMessage
    public final boolean fitsInLong() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInLong(value);
    }

    @ExportMessage
    public final long asLong(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asLong(value);
    }

    @ExportMessage
    public final boolean fitsInFloat() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInFloat(value);
    }

    @ExportMessage
    public final float asFloat(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asFloat(value);
    }

    @ExportMessage
    public final boolean fitsInDouble() {
        if (!isNumber()) {
            return false;
        }
        Object value = getScalarValue();
        return getInterop(value).fitsInDouble(value);
    }

    @ExportMessage
    public final double asDouble(@Cached.Shared("isNumber") @Cached("createBinaryProfile()") ConditionProfile isNumber) throws UnsupportedMessageException {
        if (!isNumber.profile(isNumber())) {
            throw UnsupportedMessageException.create();
        }
        Object value = getScalarValue();
        return getInterop(value).asDouble(value);
    }

    private static InteropLibrary getInterop(Object value) {
        return InteropLibrary.getFactory().create(value);
    }

}

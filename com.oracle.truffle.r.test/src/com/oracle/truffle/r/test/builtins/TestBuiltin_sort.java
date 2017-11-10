/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 2012-2014, Purdue University
 * Copyright (c) 2013, 2017, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.builtins;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;

// Checkstyle: stop line length check
public class TestBuiltin_sort extends TestBase {

    @Test
    public void testsort1() {
        assertEval("argv <- list('x1', FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort2() {
        assertEval("argv <- list(1:10, TRUE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort3() {
        assertEval("argv <- list(c(1L, 2L, 3L, 153L, 4L, 154L, 303L, 452L, 5L, 155L, 304L, 453L, 6L, 156L, 305L, 454L, 7L, 157L, 306L, 455L, 8L, 158L, 307L, 456L, 9L, 159L, 308L, 457L, 10L, 160L, 309L, 458L, 11L, 161L, 310L, 459L, 12L, 162L, 311L, 460L, 13L, 163L, 312L, 461L, 14L, 164L, 313L, 462L, 15L, 165L, 314L, 463L, 16L, 166L, 315L, 464L, 17L, 167L, 316L, 465L, 18L, 168L, 317L, 466L, 19L, 169L, 318L, 467L, 20L, 170L, 319L, 468L, 21L, 171L, 320L, 469L, 22L, 172L, 321L, 470L, 23L, 173L, 322L, 471L, 24L, 174L, 323L, 472L, 25L, 175L, 324L, 473L, 26L, 176L, 325L, 474L, 27L, 177L, 326L, 475L, 28L, 178L, 327L, 476L, 29L, 179L, 328L, 477L, 30L, 180L, 329L, 478L, 31L, 181L, 330L, 479L, 32L, 182L, 331L, 480L, 33L, 183L, 332L, 481L, 34L, 184L, 333L, 482L, 35L, 185L, 334L, 483L, 36L, 186L, 335L, 484L, 37L, 187L, 336L, 485L, 38L, 188L, 337L, 486L, 39L, 189L, 338L, 487L, 40L, 190L, 339L, 488L, 41L, 191L, 340L, 489L, 42L, 192L, 341L, 490L, 43L, 193L, 342L, 491L, 44L, 194L, 343L, 492L, 45L, 195L, 344L, 493L, 46L, 196L, 345L, 494L, 47L, 197L, 346L, 495L, 48L, 198L, 347L, 496L, 49L, 199L, 348L, 497L, 50L, 200L, 349L, 498L, 51L, 201L, 350L, 499L, 52L, 202L, 351L, 500L, 53L, 203L, 352L, 501L, 54L, 204L, 353L, 502L, 55L, 205L, 354L, 503L, 56L, 206L, 355L, 504L, 57L, 207L, 356L, 505L, 58L, 208L, 357L, 506L, 59L, 209L, 358L, 507L, 60L, 210L, 359L, 508L, 61L, 211L, 360L, 509L, 62L, 212L, 361L, 510L, 63L, 213L, 362L, 511L, 64L, 214L, 363L, 512L, 65L, 215L, 364L, 513L, 66L, 216L, 365L, 514L, 67L, 217L, 366L, 515L, 68L, 218L, 367L, 516L, 69L, 219L, 368L, 517L, 70L, 220L, 369L, 518L, 71L, 221L, 370L, 519L, 72L, 222L, 371L, 520L, 73L, 223L, 372L, 521L, 74L, 224L, 373L, 522L, 75L, 225L, 374L, 523L, 76L, 226L, 375L, 524L, 77L, 227L, 376L, 525L, 78L, 228L, 377L, 526L, 79L, 229L, 378L, 527L, 80L, 230L, 379L, 528L, 81L, 231L, 380L, 529L, 82L, 232L, 381L, 530L, 83L, 233L, 382L, 531L, 84L, 234L, 383L, 532L, 85L, 235L, 384L, 533L, 86L, 236L, 385L, 534L, 87L, 237L, 386L, 535L, 88L, 238L, 387L, 536L, 89L, 239L, 388L, 537L, 90L, 240L, 389L, 538L, 91L, 241L, 390L, 539L, 92L, 242L, 391L, 540L, 93L, 243L, 392L, 541L, 94L, 244L, 393L, 542L, 95L, 245L, 394L, 543L, 96L, 246L, 395L, 544L, 97L, 247L, 396L, 545L, 98L, 248L, 397L, 546L, 99L, 249L, 398L, 547L, 100L, 250L, 399L, 548L, 101L, 251L, 400L, 549L, 102L, 252L, 401L, 550L, 103L, 253L, 402L, 551L, 104L, 254L, 403L, 552L, 105L, 255L, 404L, 553L, 106L, 256L, 405L, 554L, 107L, 257L, 406L, 555L, 108L, 258L, 407L, 556L, 109L, 259L, 408L, 557L, 110L, 260L, 409L, 558L, 111L, 261L, 410L, 559L, 112L, 262L, 411L, 560L, 113L, 263L, 412L, 561L, 114L, 264L, 413L, 562L, 115L, 265L, 414L, 563L, 116L, 266L, 415L, 564L, 117L, 267L, 416L, 565L, 118L, 268L, 417L, 566L, 119L, 269L, 418L, 567L, 120L, 270L, 419L, 568L, 121L, 271L, 420L, 569L, 122L, 272L, 421L, 570L, 123L, 273L, 422L, 571L, 124L, 274L, 423L, 572L, 125L, 275L, 424L, 573L, 126L, 276L, 425L, 574L, 127L, 277L, 426L, 575L, 128L, 278L, 427L, 576L, 129L, 279L, 428L, 577L, 130L, 280L, 429L, 578L, 131L, 281L, 430L, 579L, 132L, 282L, 431L, 580L, 133L, 283L, 432L, 581L, 134L, 284L, 433L, 582L, 135L, 285L, 434L, 583L, 136L, 286L, 435L, 584L, 137L, 287L, 436L, 585L, 138L, 288L, 437L, 586L, 139L, 289L, 438L, 587L, 140L, 290L, 439L, 588L, 141L, 291L, 440L, 589L, 142L, 292L, 441L, 590L, 143L, 293L, 442L, 591L, 144L, 294L, 443L, 592L, 145L, 295L, 444L, 593L, 146L, 296L, 445L, 594L, 147L, 297L, 446L, 595L, 148L, 298L, 447L, 596L, 149L, 299L, 448L, 597L, 150L, 300L, 449L, 598L, 151L, 301L, 450L, 599L, 152L, 302L, 451L, 600L), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort4() {
        assertEval("argv <- list(c('graphics', 'lattice', 'stats'), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort5() {
        assertEval("argv <- list(1:54, FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort6() {
        assertEval("argv <- list(c(8.41842881182087, 0.633658419345243, 0.55014003120899, 0.264811823419969, 2.45100807149625e-16, 1.4406901715276e-16), TRUE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort7() {
        assertEval("argv <- list(FALSE, FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort8() {
        assertEval("argv <- list(c(12784, 12815, 12843, 12874, 12904, 12935, 12965, 12996, 13027, 13057, 13088, 13118, 13149, 13180, 13208, 13239, 13269, 13300, 13330, 13361, 13392, 13422, 13453, 13483, 13514, 13545, 13573, 13604, 13634, 13665, 13695, 13726, 13757, 13787, 13818, 13848, 13879, 13910, 13939, 13970, 14000, 14031, 14061, 14092, 14123, 14153, 14184, 14214, 14245, 14276), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort9() {
        assertEval("argv <- list(c('M.user', 'Soft'), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort10() {
        assertEval("argv <- list(c(0, 1, 2, 3, 4, 5, 10, 20, 0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 10.5, 20.5), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort11() {
        assertEval("argv <- list(c(-2.19467484932178, -1.98568521098019, -1.77669557263859, -1.567705934297, -1.35871629595541, -1.14972665761382, -0.940737019272223, -0.73174738093063, -0.522757742589037, -0.313768104247444, -0.104778465905851, 0.104211172435742, 0.313200810777335, 0.522190449118928, 0.731180087460521, 0.940169725802114, 1.14915936414371), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort12() {
        assertEval("argv <- list(numeric(0), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort13() {
        assertEval("argv <- list(c(3.2170141852258, 1.05106928690785, 1.66357940564162, 2.06079422122964, 4.14833588122863, 2.06378765307826, 2.36276370659185, 3.24215147884243, 2.83953129834327, 3.30869483883634, 5.46733964221407, 2.03767398891014, 2.85680802025857, 2.64551821042465, 4.67467308700083, 2.3927927372781, 3.08019075639266, 3.94941127449109, 2.10368712421998, 4.81745347591039, 3.83804007564263, 2.08807105063803, 3.61685546922612, 2.94897051693531, 3.47550643216271, 3.07230246315272, 4.23279369694672, 1.81117727446505, 2.63966096297246, 2.91308698241298, 2.16647893531705, 2.12232261640219, 3.14429741959172, 3.03010252731164, 0.612934885417947, 4.20285111588776, 3.41200339615357, 5.57848503331671, 3.84747589821948, 2.71531835120639, 5.95966880712648, 5.99450368408389, 1.9435658438782, 3.6313096161238, 2.95103074153623, 3.34932880672239, 3.38982038873719, 1.90037719729933, 3.44786724041094, 4.91152502331018, 2.99818765603358, 2.99935094946993, 4.56084966650585, 4.06067390085133, 1.51378284178709, 3.39083463343057, 4.81582489484611, 4.57755401008752, 3.58222926398261, 3.88349846728127, 1.9653424479797, 3.46240922704975, 2.20782872498056, 2.70425959207144, 4.09788134394798, 2.02538454847724, 3.42919591104144, 3.59060969963992, 3.21718963000801, 4.98446648200379, 2.13993033159313, 3.76840792160398, 3.07334709271771, 2.90144541633446, 3.62636889402076, 2.14187706948445, 1.84882674364997, 2.66779678468496, 4.91403480992383, 3.2180424347809, 3.49371205839627, 2.97243102249084, 3.6327703921222, 2.21059811715123, 4.32018812673702, 3.74698292040973, 2.35582483747667, 3.21683090598037, 3.8786092796675, 3.72000864222298, 4.64421167604526, 2.54928990527353, 4.27841427565877, 4.1988256701096, 2.99979452826552, 2.18635096734649, 2.07313246928386, 3.62006461811335, 3.09115092644749, 4.94138032701983), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort14() {
        assertEval("argv <- list(c(2.17292368994844e-311, 4.34584737989688e-311, 8.69169475979376e-311, 1.73833895195875e-310, 3.4766779039175e-310, 6.953355807835e-310, 1.390671161567e-309, 2.781342323134e-309, 5.562684646268e-309, 1.1125369292536e-308, 2.2250738585072e-308, 4.4501477170144e-308, 8.90029543402881e-308, 1.78005908680576e-307, 2.2250738585072e-303, 2.2250738585072e-298, 1.79769313486232e+298, 1.79769313486232e+303, 2.24711641857789e+307, 4.49423283715579e+307, 8.98846567431158e+307, 1.79769313486232e+308, Inf, -Inf, Inf), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort15() {
        assertEval("argv <- list(structure(c(74, 68, 56, 57, 60, 71, 53, 61, 67, 70, 63, 49, 50, 58, 72, 69, 73, 48, 62, 65, 66, 64, 59, 76, 75, 40, 51, 81, 55, 42, 44, 54, 80, 77, 47, 82, 46, 43, 39, 45, 52, 41), .Dim = c(42L, 1L), .Dimnames = list(c('1', '2', '3', '4', '5', '8', '9', '10', '16', '17', '18', '22', '23', '24', '25', '31', '32', '33', '36', '37', '38', '40', '43', '46', '61', '74', '77', '79', '82', '83', '84', '107', '113', '129', '133', '149', '168', '174', '182', '186', '192', '217'), 'age')), FALSE); .Internal(sort(argv[[1]], argv[[2]]))");
    }

    @Test
    public void testsort17() {
        assertEval("argv <- structure(list(x = c(2.14210348343477, 2.73273128271103,     2.99806873407215, -0.528692848049104, -2.21930913347751,     0.327189480420202, -0.761920109856874, -1.45133259287104,     2.58289965474978, 0.882365602534264, -0.678148102015257,     2.09740992262959, 1.11015366297215, 2.70643682219088, 0.185772243887186,     -2.38685618760064, -2.97262442205101, 0.473321800120175,     -1.20133379939944, 1.63897713040933, -1.89173630112782, 1.73645787220448,     -0.0272455788217485, -0.0804266170598567, 2.78059691889212,     -2.11383658647537, 0.0939270523376763, -0.390122111421078,     -2.80329246399924, 1.61059647146612, 2.74780045822263, -0.0192620046436787,     0.0169407553039491, -1.57991883857176, 1.76223263237625,     -2.21499892836437, 0.367137833498418, -0.284903160296381,     2.52876619063318, 0.633825332857668, 0.613207478541881, 2.08658177917823,     -2.96446485584602, -2.07629728876054, 0.46877646446228, 1.88368982356042,     0.416373030748218, 1.91595612186939, -2.8897425387986, -0.625228523276746,     0.134519706945866, -0.416335945017636, -2.52922565164044,     0.17425535665825, -1.39055569516495, -0.423170546069741,     2.93497854005545, -1.64256255235523, 0.708815339952707, -2.20641956990585,     1.95717442128807, -2.05757057340816, 2.76040208246559, 2.2406962341629,     -1.68299576221034, -1.50189629523084, 1.54184397496283, 0.0106206983327866,     -0.644365496467799, 1.71497052256018, -2.21753972489387,     -0.272966742049903, -0.0741098136641085, 2.63908819807693,     2.97978561837226, -1.19580693589523, -0.940262471325696,     0.556911027990282, -2.33519576629624, -0.223178054206073,     2.98530492978171, -2.27890933351591, 2.41673697670922, -2.31641680374742,     -0.397401746828109, -1.83408120274544, -0.934458317700773,     -2.91743992455304, -0.452570331282914, -1.79014129796997,     -2.82882511569187, 1.8992390432395, 1.25369117455557, -2.21495646424592,     -2.45502642402425, -2.67720098560676, -1.5648388476111, -0.0616166163235903,     2.89307818282396, -2.87064984021708)), .Names = 'x');" +
                        "do.call('sort', argv)");
    }

    @Test
    public void testsort18() {
        assertEval("argv <- structure(list(x = structure(c(8092, 8092, 8048, 8093,     8066), origin = structure(c(1, 1, 1970), .Names = c('month',     'day', 'year')), class = c('dates', 'times'))), .Names = 'x');" +
                        "do.call('sort', argv)");
    }

    @Test
    public void testSort() {
        assertEval("{ sort(c(1L,10L,2L)) }");
        assertEval("{ sort(c(3,10,2)) }");
        assertEval("{ sort(c(1,2,0/0,NA)) }");
        assertEval("{ sort(c(2,1,0/0,NA), na.last=NA) }");
        assertEval("{ sort(c(3,NA,0/0,2), na.last=FALSE) }");
        assertEval("{ sort(c(a=NA,b=NA,c=3,d=1),na.last=TRUE, decreasing=TRUE) }");
        assertEval("{ sort(c(a=NA,b=NA,c=3,d=1),na.last=FALSE, decreasing=FALSE) }");
        assertEval("{ sort(c(a=0/0,b=1/0,c=3,d=NA),na.last=TRUE, decreasing=FALSE) }");
        assertEval("{ sort(c(a=NA,b=NA,c=3L,d=-1L),na.last=TRUE, decreasing=FALSE) }");
        assertEval("{ sort(c(3,NA,1,d=10), decreasing=FALSE, index.return=TRUE) }");
        assertEval("{ sort(3:1, index.return=TRUE) }");
        assertEval("{ sort(c(1L,10L,2L), method=\"quick\") }");
        assertEval("{ sort(c(\"abc\", \"aba\", \"aa\")) }");
    }

    @Test
    public void testArgsCasts() {
        assertEval("{ .Internal(sort(c(1L,10L,2L), 'not-numeric')) }");
        assertEval("{ .Internal(sort(c(1L,10L,2L), NULL)) }");
        assertEval("{ .Internal(sort(as.raw(c(0x44,0x40)), FALSE)) }"); // Only numeric vectors can
                                                                        // be sorted
        assertEval("{ .Internal(sort(NULL, FALSE)) }");
        assertEval("{ lv<-list(a=5,b=c(1,2)); .Internal(sort(lv,FALSE)) }");
    }

    @Test
    public void testQSort() {
        assertEval("{ .Internal(qsort(NULL, F)) }");
        assertEval("{ .Internal(qsort(NULL, NULL)) }");
        // seems that when the value provided for decreased is whatever else than FALSE
        // then it is interpreted as TRUE. In such a case qsort returns a list containing
        // two vectors: the values sorted as in decreased=F and the original indices giving
        // the decreased order. NOTE that even though the FastR impl of qsort always returns
        // only 1 increasing/decreasing vector, it has no effect on the overall sort function.
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(1, decreased=NULL)) }");
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(c(4, 2, 3), T)) }");
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(c(4, 2, 3), 1)) }");
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(c(4, 2, 3), 'a')) }");
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(c(4, 2, 3), c('a'))) }");
        assertEval(Ignored.ImplementationError, "{ .Internal(qsort(c(4, 2, 3), list('a'))) }");
        assertEval("{ .Internal(qsort(list(1), F)) }");
        assertEval("{ .Internal(qsort(1, F)) }");
        assertEval("{ .Internal(qsort(c(1), F)) }");
        assertEval(Ignored.ImplementationError, "sort(c('FUN', 'simplify', 'USENAMES', 'X'))");
    }
}

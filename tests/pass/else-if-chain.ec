// A long `else if` chain is one statement (the Visitor flattens `else if`). The grammar used to match it two ways,
// and parsing took cubic time: 120 branches took 45 seconds.
use "core:io.ec"

fn pick(x: Int): Int do
    if x == 0 do
        ret 0
    else if x == 1 do
        ret 2
    else if x == 2 do
        ret 4
    else if x == 3 do
        ret 6
    else if x == 4 do
        ret 8
    else if x == 5 do
        ret 10
    else if x == 6 do
        ret 12
    else if x == 7 do
        ret 14
    else if x == 8 do
        ret 16
    else if x == 9 do
        ret 18
    else if x == 10 do
        ret 20
    else if x == 11 do
        ret 22
    else if x == 12 do
        ret 24
    else if x == 13 do
        ret 26
    else if x == 14 do
        ret 28
    else if x == 15 do
        ret 30
    else if x == 16 do
        ret 32
    else if x == 17 do
        ret 34
    else if x == 18 do
        ret 36
    else if x == 19 do
        ret 38
    else if x == 20 do
        ret 40
    else if x == 21 do
        ret 42
    else if x == 22 do
        ret 44
    else if x == 23 do
        ret 46
    else if x == 24 do
        ret 48
    else if x == 25 do
        ret 50
    else if x == 26 do
        ret 52
    else if x == 27 do
        ret 54
    else if x == 28 do
        ret 56
    else if x == 29 do
        ret 58
    else if x == 30 do
        ret 60
    else if x == 31 do
        ret 62
    else if x == 32 do
        ret 64
    else if x == 33 do
        ret 66
    else if x == 34 do
        ret 68
    else if x == 35 do
        ret 70
    else if x == 36 do
        ret 72
    else if x == 37 do
        ret 74
    else if x == 38 do
        ret 76
    else if x == 39 do
        ret 78
    else if x == 40 do
        ret 80
    else if x == 41 do
        ret 82
    else if x == 42 do
        ret 84
    else if x == 43 do
        ret 86
    else if x == 44 do
        ret 88
    else if x == 45 do
        ret 90
    else if x == 46 do
        ret 92
    else if x == 47 do
        ret 94
    else if x == 48 do
        ret 96
    else if x == 49 do
        ret 98
    else if x == 50 do
        ret 100
    else if x == 51 do
        ret 102
    else if x == 52 do
        ret 104
    else if x == 53 do
        ret 106
    else if x == 54 do
        ret 108
    else if x == 55 do
        ret 110
    else if x == 56 do
        ret 112
    else if x == 57 do
        ret 114
    else if x == 58 do
        ret 116
    else if x == 59 do
        ret 118
    else if x == 60 do
        ret 120
    else if x == 61 do
        ret 122
    else if x == 62 do
        ret 124
    else if x == 63 do
        ret 126
    else if x == 64 do
        ret 128
    else if x == 65 do
        ret 130
    else if x == 66 do
        ret 132
    else if x == 67 do
        ret 134
    else if x == 68 do
        ret 136
    else if x == 69 do
        ret 138
    else if x == 70 do
        ret 140
    else if x == 71 do
        ret 142
    else if x == 72 do
        ret 144
    else if x == 73 do
        ret 146
    else if x == 74 do
        ret 148
    else if x == 75 do
        ret 150
    else if x == 76 do
        ret 152
    else if x == 77 do
        ret 154
    else if x == 78 do
        ret 156
    else if x == 79 do
        ret 158
    else if x == 80 do
        ret 160
    else if x == 81 do
        ret 162
    else if x == 82 do
        ret 164
    else if x == 83 do
        ret 166
    else if x == 84 do
        ret 168
    else if x == 85 do
        ret 170
    else if x == 86 do
        ret 172
    else if x == 87 do
        ret 174
    else if x == 88 do
        ret 176
    else if x == 89 do
        ret 178
    else if x == 90 do
        ret 180
    else if x == 91 do
        ret 182
    else if x == 92 do
        ret 184
    else if x == 93 do
        ret 186
    else if x == 94 do
        ret 188
    else if x == 95 do
        ret 190
    else if x == 96 do
        ret 192
    else if x == 97 do
        ret 194
    else if x == 98 do
        ret 196
    else if x == 99 do
        ret 198
    else if x == 100 do
        ret 200
    else if x == 101 do
        ret 202
    else if x == 102 do
        ret 204
    else if x == 103 do
        ret 206
    else if x == 104 do
        ret 208
    else if x == 105 do
        ret 210
    else if x == 106 do
        ret 212
    else if x == 107 do
        ret 214
    else if x == 108 do
        ret 216
    else if x == 109 do
        ret 218
    else if x == 110 do
        ret 220
    else if x == 111 do
        ret 222
    else if x == 112 do
        ret 224
    else if x == 113 do
        ret 226
    else if x == 114 do
        ret 228
    else if x == 115 do
        ret 230
    else if x == 116 do
        ret 232
    else if x == 117 do
        ret 234
    else if x == 118 do
        ret 236
    else if x == 119 do
        ret 238
    else if x == 120 do
        ret 240
    else if x == 121 do
        ret 242
    else if x == 122 do
        ret 244
    else if x == 123 do
        ret 246
    else if x == 124 do
        ret 248
    else if x == 125 do
        ret 250
    else if x == 126 do
        ret 252
    else if x == 127 do
        ret 254
    else if x == 128 do
        ret 256
    else if x == 129 do
        ret 258
    else if x == 130 do
        ret 260
    else if x == 131 do
        ret 262
    else if x == 132 do
        ret 264
    else if x == 133 do
        ret 266
    else if x == 134 do
        ret 268
    else if x == 135 do
        ret 270
    else if x == 136 do
        ret 272
    else if x == 137 do
        ret 274
    else if x == 138 do
        ret 276
    else if x == 139 do
        ret 278
    else if x == 140 do
        ret 280
    else if x == 141 do
        ret 282
    else if x == 142 do
        ret 284
    else if x == 143 do
        ret 286
    else if x == 144 do
        ret 288
    else if x == 145 do
        ret 290
    else if x == 146 do
        ret 292
    else if x == 147 do
        ret 294
    else if x == 148 do
        ret 296
    else if x == 149 do
        ret 298
    else if x == 150 do
        ret 300
    else if x == 151 do
        ret 302
    else if x == 152 do
        ret 304
    else if x == 153 do
        ret 306
    else if x == 154 do
        ret 308
    else if x == 155 do
        ret 310
    else if x == 156 do
        ret 312
    else if x == 157 do
        ret 314
    else if x == 158 do
        ret 316
    else if x == 159 do
        ret 318
    else if x == 160 do
        ret 320
    else if x == 161 do
        ret 322
    else if x == 162 do
        ret 324
    else if x == 163 do
        ret 326
    else if x == 164 do
        ret 328
    else if x == 165 do
        ret 330
    else if x == 166 do
        ret 332
    else if x == 167 do
        ret 334
    else if x == 168 do
        ret 336
    else if x == 169 do
        ret 338
    else if x == 170 do
        ret 340
    else if x == 171 do
        ret 342
    else if x == 172 do
        ret 344
    else if x == 173 do
        ret 346
    else if x == 174 do
        ret 348
    else if x == 175 do
        ret 350
    else if x == 176 do
        ret 352
    else if x == 177 do
        ret 354
    else if x == 178 do
        ret 356
    else if x == 179 do
        ret 358
    else if x == 180 do
        ret 360
    else if x == 181 do
        ret 362
    else if x == 182 do
        ret 364
    else if x == 183 do
        ret 366
    else if x == 184 do
        ret 368
    else if x == 185 do
        ret 370
    else if x == 186 do
        ret 372
    else if x == 187 do
        ret 374
    else if x == 188 do
        ret 376
    else if x == 189 do
        ret 378
    else if x == 190 do
        ret 380
    else if x == 191 do
        ret 382
    else if x == 192 do
        ret 384
    else if x == 193 do
        ret 386
    else if x == 194 do
        ret 388
    else if x == 195 do
        ret 390
    else if x == 196 do
        ret 392
    else if x == 197 do
        ret 394
    else if x == 198 do
        ret 396
    else if x == 199 do
        ret 398
    else if x == 200 do
        ret 400
    else if x == 201 do
        ret 402
    else if x == 202 do
        ret 404
    else if x == 203 do
        ret 406
    else if x == 204 do
        ret 408
    else if x == 205 do
        ret 410
    else if x == 206 do
        ret 412
    else if x == 207 do
        ret 414
    else if x == 208 do
        ret 416
    else if x == 209 do
        ret 418
    else if x == 210 do
        ret 420
    else if x == 211 do
        ret 422
    else if x == 212 do
        ret 424
    else if x == 213 do
        ret 426
    else if x == 214 do
        ret 428
    else if x == 215 do
        ret 430
    else if x == 216 do
        ret 432
    else if x == 217 do
        ret 434
    else if x == 218 do
        ret 436
    else if x == 219 do
        ret 438
    else if x == 220 do
        ret 440
    else if x == 221 do
        ret 442
    else if x == 222 do
        ret 444
    else if x == 223 do
        ret 446
    else if x == 224 do
        ret 448
    else if x == 225 do
        ret 450
    else if x == 226 do
        ret 452
    else if x == 227 do
        ret 454
    else if x == 228 do
        ret 456
    else if x == 229 do
        ret 458
    else if x == 230 do
        ret 460
    else if x == 231 do
        ret 462
    else if x == 232 do
        ret 464
    else if x == 233 do
        ret 466
    else if x == 234 do
        ret 468
    else if x == 235 do
        ret 470
    else if x == 236 do
        ret 472
    else if x == 237 do
        ret 474
    else if x == 238 do
        ret 476
    else if x == 239 do
        ret 478
    else if x == 240 do
        ret 480
    else if x == 241 do
        ret 482
    else if x == 242 do
        ret 484
    else if x == 243 do
        ret 486
    else if x == 244 do
        ret 488
    else if x == 245 do
        ret 490
    else if x == 246 do
        ret 492
    else if x == 247 do
        ret 494
    else if x == 248 do
        ret 496
    else if x == 249 do
        ret 498
    else if x == 250 do
        ret 500
    else if x == 251 do
        ret 502
    else if x == 252 do
        ret 504
    else if x == 253 do
        ret 506
    else if x == 254 do
        ret 508
    else if x == 255 do
        ret 510
    else if x == 256 do
        ret 512
    else if x == 257 do
        ret 514
    else if x == 258 do
        ret 516
    else if x == 259 do
        ret 518
    else if x == 260 do
        ret 520
    else if x == 261 do
        ret 522
    else if x == 262 do
        ret 524
    else if x == 263 do
        ret 526
    else if x == 264 do
        ret 528
    else if x == 265 do
        ret 530
    else if x == 266 do
        ret 532
    else if x == 267 do
        ret 534
    else if x == 268 do
        ret 536
    else if x == 269 do
        ret 538
    else if x == 270 do
        ret 540
    else if x == 271 do
        ret 542
    else if x == 272 do
        ret 544
    else if x == 273 do
        ret 546
    else if x == 274 do
        ret 548
    else if x == 275 do
        ret 550
    else if x == 276 do
        ret 552
    else if x == 277 do
        ret 554
    else if x == 278 do
        ret 556
    else if x == 279 do
        ret 558
    else if x == 280 do
        ret 560
    else if x == 281 do
        ret 562
    else if x == 282 do
        ret 564
    else if x == 283 do
        ret 566
    else if x == 284 do
        ret 568
    else if x == 285 do
        ret 570
    else if x == 286 do
        ret 572
    else if x == 287 do
        ret 574
    else if x == 288 do
        ret 576
    else if x == 289 do
        ret 578
    else if x == 290 do
        ret 580
    else if x == 291 do
        ret 582
    else if x == 292 do
        ret 584
    else if x == 293 do
        ret 586
    else if x == 294 do
        ret 588
    else if x == 295 do
        ret 590
    else if x == 296 do
        ret 592
    else if x == 297 do
        ret 594
    else if x == 298 do
        ret 596
    else if x == 299 do
        ret 598
    else do
        ret 0 - 1

fn main(): Int do
    io.log(pick(0))
    io.log(pick(1))
    io.log(pick(299))
    io.log(pick(300))
    ret 0

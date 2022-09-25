package ir.yamin.digits.constants

import android.content.Context
import ir.yamin.digits.R
import java.math.BigInteger

/**
 * numbers representation in Formal Persian
 */
internal class PersianNumber(context: Context) {
    
    //صدم، میلیونم، دهم و ...
    internal val TH = context.getString(R.string.th)
    internal val MINUS = context.getString(R.string.minus)
    internal val RADIX = context.getString(R.string.point)
    internal val ZERO = context.getString(R.string.zero)
    internal val ONE = context.getString(R.string.one)
    internal val AND = context.getString(R.string.and)
    private val TWO = context.getString(R.string.two)
    private val THREE = context.getString(R.string.three)
    private val FOUR = context.getString(R.string.four)
    private val FIVE = context.getString(R.string.five)
    private val SIX = context.getString(R.string.six)
    private val SEVEN = context.getString(R.string.seven)
    private val EIGHT = context.getString(R.string.eight)
    private val NINE = context.getString(R.string.nine)
    private val TEN = context.getString(R.string.ten)
    private val HUNDRED = context.getString(R.string.hundred)
    private val THOUSAND = context.getString(R.string.thousand)
    private val MILLION = context.getString(R.string.million)
    private val MILLIARD = context.getString(R.string.billion)
    private val TRILLION = context.getString(R.string.trillion)
    private val QUADRILLION = context.getString(R.string.quadrillion)
    private val QUINTILLION = context.getString(R.string.quintillion)
    private val SEXTILLION = context.getString(R.string.sextillion)
    private val SEPTILLION = context.getString(R.string.septillion)
    private val OCTILLION = context.getString(R.string.octillion)
    private val NONILLION = context.getString(R.string.nonillion)
    private val DECILLION = context.getString(R.string.decillion)
    private val UNDECILLION = context.getString(R.string.undecillion)
    private val DUODECILLION = context.getString(R.string.duodecillion)
    private val TREDECILLION = context.getString(R.string.tredecillion)
    private val QUATTUORDECILLION = context.getString(R.string.quattuordecillion)
    private val QUINDECILLION = context.getString(R.string.quindecillion)
    private val SEXDECILLION = context.getString(R.string.sexdecillion)
    private val SEPTENDECILLION = context.getString(R.string.septendecillion)
    private val OCTODECILLION = context.getString(R.string.octodecillion)
    private val NOVEMDECILLION = context.getString(R.string.novemdecillion)
    private val VIGINTILLION = context.getString(R.string.vigintillion)
    
    /**
     * Single digits numbers representation
     */
    val singleDigits = mapOf(
            0L to ZERO, 1L to ONE, 2L to TWO, 3L to THREE, 4L to FOUR, 5L to FIVE, 6L to SIX,
            7L to SEVEN, 8L to EIGHT, 9L to NINE,
                            )
    
    /**
     * Two digits numbers representation
     */
    val twoDigits = mapOf(
            10L to TEN, 11L to context.getString(R.string.eleven), 12L to context.getString(R.string.twelve), 13L to context.getString(
                        R.string.thirteen),
            14L to context.getString(R.string.fourteen), 15L to context.getString(R.string.fifteen), 16L to context.getString(
                        R.string.sixteen), 17L to context.getString(R.string.seventeen),
            18L to context.getString(R.string.eighteen), 19L to context.getString(R.string.nineteen), 20L to context.getString(
                        R.string.twenty), 30L to context.getString(R.string.thirty), 40L to context.getString(R.string.forty),
            50L to context.getString(R.string.fifty), 60L to context.getString(R.string.sixty), 70L to context.getString(
                        R.string.seventy), 80L to context.getString(R.string.eighty), 90L to context.getString(R.string.ninety),
                         )
    
    /**
     * Three digits numbers representation
     */
    val threeDigits = mapOf(
            100L to "$ONE $HUNDRED", 200L to context.getString(R.string.two_hundred), 300L to context.getString(R.string.three_hundred),
            400L to "$FOUR$HUNDRED", 500L to context.getString(R.string.five_hundred), 600L to "$SIX$HUNDRED",
            700L to "$SEVEN$HUNDRED", 800L to "$EIGHT$HUNDRED", 900L to "$NINE$HUNDRED",
                           )
    
    /**
     * Products of Ten representation
     */
    val tenPowers = mapOf(
            1_000L to THOUSAND, 1_000_000L to MILLION, 1_000_000_000L to MILLIARD,
            1_000_000_000_000L to TRILLION, 1_000_000_000_000_000L to QUADRILLION,
            1_000_000_000_000_000_000L to QUINTILLION,
                         )
    
    /**
     * Big integer Products of Ten representation
     */
    internal val bigTen = BigInteger("10")
    val bigIntegerTenPowers = mapOf(
            bigTen.pow(21) to SEXTILLION, bigTen.pow(24) to SEPTILLION,
            bigTen.pow(27) to OCTILLION, bigTen.pow(30) to NONILLION,
            bigTen.pow(33) to DECILLION, bigTen.pow(36) to UNDECILLION,
            bigTen.pow(39) to DUODECILLION, bigTen.pow(42) to TREDECILLION,
            bigTen.pow(45) to QUATTUORDECILLION, bigTen.pow(48) to QUINDECILLION,
            bigTen.pow(51) to SEXDECILLION, bigTen.pow(54) to SEPTENDECILLION,
            bigTen.pow(57) to OCTODECILLION, bigTen.pow(60) to NOVEMDECILLION,
            bigTen.pow(63) to VIGINTILLION,
                                   )
}
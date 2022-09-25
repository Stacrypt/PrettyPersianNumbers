@file:Suppress("unused")

package ir.yamin.digits

import android.content.Context
import ir.yamin.digits.constants.IranCurrency
import ir.yamin.digits.constants.PersianNumber
import java.math.BigDecimal
import java.math.BigInteger

private const val NaN = "NaN"
private const val ZERO_ONLY_REGEX = "[0]+"
private const val DECIMAL_REGEX = "\\d*\\.\\d+"
private const val zeroChar = '0'
private const val dashChar = '-'
private const val dotChar = '.'

class Digits(private val context: Context) {
    
    private val zeroOnlyRegex : Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(ZERO_ONLY_REGEX) }
    private val decimalRegex : Regex by lazy(LazyThreadSafetyMode.NONE) { Regex(DECIMAL_REGEX) }
    
    private val zeroBigInteger : BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ZERO }
    private val oneBigInteger : BigInteger by lazy(LazyThreadSafetyMode.NONE) { BigInteger.ONE }

    private var convertPowerOfThousandsOnly: Boolean = false

    private var persianNumber = PersianNumber(context)
    
    /**
     * Spell number to Persian/Farsi words
     *
     * @param number in any type like Byte/Short/Int/Long/String/Big Integer
     * @return Persian representation of that number in String type
     */
    @JvmOverloads
    fun spellToFarsi(number : Any, convertPowerOfThousandsOnly: Boolean = false) : String {
        this.convertPowerOfThousandsOnly = convertPowerOfThousandsOnly
        return try {
            when (number) {
                is Byte -> spellToFarsi("${number.toInt()}")
                is Short -> spellToFarsi("${number.toInt()}")
                is Int -> spellToFarsi("$number")
                is Long -> spellToFarsi("$number")
                is Float -> bigDecimalHandler("$number")
                is Double -> bigDecimalHandler("$number")
                is String -> stringHandler(number)
                is BigInteger -> bigIntegerHandler(number)
                else -> NaN
            }
        } catch (exception : Exception) {
            NaN
        }
    }
    
    /**
     * Spell number to Persian/Farsi words plus Iran currency name
     *
     * @param number in any type like Byte/Short/Int/Long/String/Big Integer
     * @param currency if no parameter is passed 'ریال' is default
     * @return Persian representation of that number plus currency name in String type
     */
    @JvmOverloads
    fun spellToIranMoney(number : Any, convertPowerOfThousandsOnly: Boolean = false, currency : IranCurrency = IranCurrency.RIAL) : String {
        return "${spellToFarsi(number, convertPowerOfThousandsOnly)} ${context.getString(currency.value)}"
    }
    
    /**
     * handle numbers as a String
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun stringHandler(number : String) : String {
        if (number.isBlank()) return NaN
        when (number.first()) {
            dashChar -> return handleStringsWithMinusPrefix(number)
            zeroChar -> return handleStringsWithZeroPrefix(number)
            //numbers like .0, .14 are decimals too
            dotChar -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
            else -> if (decimalRegex.matches(number)) return bigDecimalHandler(BigDecimal(number))
        }
        
        return when (number.length) {
            1 -> if(convertPowerOfThousandsOnly) number else persianNumber.singleDigits[number.toLong()] ?: NaN
            2 -> if(convertPowerOfThousandsOnly) number else twoDigitHandler(number)
            3 -> if(convertPowerOfThousandsOnly) number else threeDigitsHandler(number)
            else -> digitsHandler(number)
        }
    }
    
    /**
     * Handle strings with minus prefix
     *
     * when input starts with - like -a, -12, -, --, -a12, ...
     *
     * @param number string that starts with - (minus character)
     * @return
     */
    private fun handleStringsWithMinusPrefix(number : String) : String {
        val numberWithoutMinus = number.substring(1)
        return if (numberWithoutMinus.isNotBlank()) {
            //when normal input like -12 -123123 -5612 -0
            if (isNumberOnly(numberWithoutMinus)) {
                return if (zeroOnlyRegex.matches(numberWithoutMinus)) persianNumber.ZERO
                else "${persianNumber.MINUS} ${stringHandler(numberWithoutMinus)}"
            }
            //when input contains anything other than numbers 0-9 like --, -., -.5, -1.5
            else {
                if (decimalRegex.matches(numberWithoutMinus)) return bigDecimalHandler(number)
                else NaN
            }
        }
        //when input is only -
        else NaN
    }
    
    /**
     * handling numbers that starts with zero and removes starting zeros
     *
     * @param number string that starts with zero
     * @return string with starting zeros removed
     */
    private fun handleStringsWithZeroPrefix(number : String) : String {
        if (zeroOnlyRegex.matches(number)) return persianNumber.ZERO
        /**
         * this can probably be replaced with some smart-ass regex with lookahead,
         * but that makes it more complex
         *
         * this loop re-call this method with version of this number that doesn't contain starting zeros
         * or finds first occurrence of a non-zero number and makes that starting index of new string
         * suppose input number is 000012
         * then starting index is 4 and new string is 12
         */
        return number.indices.firstOrNull { number[it] != zeroChar }
                   ?.let { stringHandler(number.substring(it)) } ?: persianNumber.ZERO
    }
    
    /**
     * Two digit numbers handler
     *
     * @param number in String type
     * @return Persian representation of that number
     */
    private fun twoDigitHandler(number : String) : String {
        if (number.length < 2) return stringHandler(number)
        //return if number is exactly from twoDigits list
        persianNumber.twoDigits[number.toLong()]?.let { return it }
        val oneNotation = "${number[1]}".toLong()
        val tenNotation = ("${number[0]}".toLong()) * 10
        return "${persianNumber.twoDigits[tenNotation]} ${persianNumber.AND} ${persianNumber.singleDigits[oneNotation]}"
    }
    
    /**
     * Three digits number handler
     *
     * @param number in String type
     * @return persian representation of that number
     */
    private fun threeDigitsHandler(number : String) : String {
        //return if number is exactly from threeDigits list
        persianNumber.threeDigits[number.toLong()]?.let { return it }
        val oneNotation = "${number[2]}".toLong()
        val tenNotation = (("${number[1]}".toLong()) * 10) + oneNotation
        val hundredNotation = ("${number[0]}".toLong()) * 100
        return "${persianNumber.threeDigits[hundredNotation]} ${persianNumber.AND} ${twoDigitHandler("$tenNotation")}"
    }
    
    /**
     * Digits handler
     *
     * @param number in String type
     * @return persian representation of that number
     * @see Long.MAX_VALUE
     */
    private fun digitsHandler(number : String) : String {
        val bigInteger = number.toBigInteger()
        return when (bigInteger.compareTo(BigInteger.valueOf(Long.MAX_VALUE))) {
            0, -1 -> longHandler(number.toLong())
            1 -> bigIntegerHandler(bigInteger)
            else -> NaN
        }
    }
    
    /**
     * handle numbers that can be fitted in a long variable
     *
     * @param longNumber input in long format
     * @return persian representation of that number
     */
    private fun longHandler(longNumber : Long) : String {
        persianNumber.singleDigits[longNumber]?.let { return it }
        persianNumber.twoDigits[longNumber]?.let { return it }
        persianNumber.threeDigits[longNumber]?.let { return it }
        persianNumber.tenPowers[longNumber]?.let { return "${if(convertPowerOfThousandsOnly) 1 else persianNumber.ONE} $it" }
        
        //biggest ten power before input number
        val biggestTenPower = findBiggestTenPowerBeforeInputNumber(longNumber)
        if (biggestTenPower == 0L) return stringHandler("$longNumber")
        
        val tenPowerDivisor = stringHandler("${longNumber / biggestTenPower}")
        val tenPowerName = persianNumber.tenPowers[biggestTenPower] ?: ""
        val remainderNumber = longNumber % biggestTenPower
        if (remainderNumber == 0L) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName ${persianNumber.AND} $remainderNumberName"
    }
    
    /**
     * Find biggest ten power before input number,
     * biggest ten power before 2220 is 1000
     *
     * @param longNumber
     * @return
     */
    private fun findBiggestTenPowerBeforeInputNumber(longNumber : Long) : Long {
        var biggestTenPower = 0L
        if (longNumber >= 1_000L) {
            for (power in persianNumber.tenPowers) if (longNumber / power.key in 1 until longNumber) biggestTenPower = power.key
        }
        return biggestTenPower
    }
    
    private fun bigIntegerHandler(input : BigInteger) : String {
        val biggestTenPower = findBiggestTenPowerBeforeInputNumber(input)
        
        if (biggestTenPower == zeroBigInteger) return longHandler(input.toLong())
        
        val tenPowerDivisor = stringHandler("${input.divide(biggestTenPower)}")
        
        var tenPowerName = NaN
        persianNumber.bigIntegerTenPowers[biggestTenPower]?.let { tenPowerName = it }
        persianNumber.tenPowers[biggestTenPower.toLong()]?.let { tenPowerName = it }
        
        val remainderNumber = input.mod(biggestTenPower)
        if (remainderNumber == zeroBigInteger) return "$tenPowerDivisor $tenPowerName"
        val remainderNumberName = stringHandler("$remainderNumber")
        return "$tenPowerDivisor $tenPowerName ${persianNumber.AND} $remainderNumberName"
    }
    
    private fun findBiggestTenPowerBeforeInputNumber(input : BigInteger) : BigInteger {
        var biggestTenPower = zeroBigInteger
        
        if (input >= BigInteger("1000")) {
            for (power in persianNumber.tenPowers) {
                val temp = input.divide(power.key.toBigInteger())
                if (temp >= oneBigInteger && temp < input) biggestTenPower = power.key.toBigInteger()
            }
        }
        if (input >= persianNumber.bigTen.pow(21)) {
            for (power in persianNumber.bigIntegerTenPowers) {
                val temp = input.divide(power.key)
                if (temp >= oneBigInteger && temp < input) biggestTenPower = power.key
            }
        }
        return biggestTenPower
    }
    
    /**
     * Big decimal handler
     *
     * convenient method to avoid calling bigDecimalHandler(BigDecimal(number))
     *
     * @param bigDecimalString string that is probably can be parsed to BigDecimal
     */
    private fun bigDecimalHandler(bigDecimalString : String) = bigDecimalHandler(BigDecimal(bigDecimalString))
    
    /**
     * Big decimal handler,
     * handle conversion of decimal numbers like 3.14, 0.0, 0.1, 0.0002, 1.0002
     *
     * @param bigDecimal big decimal input number
     * @return string representation of given decimal number in farsi
     */
    private fun bigDecimalHandler(bigDecimal : BigDecimal) : String {
        val zeroDecimal = BigDecimal.ZERO
        when (bigDecimal.compareTo(zeroDecimal)) {
            -1 -> return "${persianNumber.MINUS} ${bigDecimalHandler(bigDecimal.abs())}"
            0 -> return persianNumber.ZERO
            1 -> {
                //dividing integer and fraction part from decimal
                val integerPart = bigDecimal.toBigInteger()
                val fraction = bigDecimal.remainder(BigDecimal.ONE)
                //if input only contains integers and no fraction like 1.0, 14.5
                val isIntegerOnly = fraction == zeroDecimal || fraction.compareTo(zeroDecimal) == 0
                if (isIntegerOnly) return bigIntegerHandler(integerPart)
                //if bigDecimal is 3.14 then decimals is 14
                val decimals = fraction.scaleByPowerOfTen(fraction.scale())
                //if bigDecimal is 3.14 then ten power is 100 or صدم
                val tenPower = persianNumber.bigTen.pow(fraction.scale())
                //add م to صد so it becomes صدم
                var tenPowerName = "${bigIntegerHandler(tenPower)}${persianNumber.TH}"
                tenPowerName = normalizeTenPowerName(tenPowerName)
                //if input is only fraction like 0.5, 0.0002
                val isFractionOnly = integerPart == zeroBigInteger || integerPart.compareTo(
                        zeroBigInteger) == 0
                val fractionName = bigIntegerHandler(BigInteger("$decimals"))
                if (isFractionOnly) return "$fractionName $tenPowerName"
                //if input is normal like 3.14, 3.121323, 15.00001
                val integerName = bigIntegerHandler(integerPart)
                return "$integerName ${persianNumber.RADIX} $fractionName، $tenPowerName"
            }
        }
        return NaN
    }
    
    /**
     * Normalize ten power name,
     *
     * since we don't want return value to be سه ممیز چهارده، یک صدم
     * and be سه ممیز چهارده، صدم
     * then we remove that part
     *
     * @param input un-normal ten power name
     * @return normalized ten power name
     */
    private fun normalizeTenPowerName(input : String) : String {
        var tenPowerName = input
        val persianOne = persianNumber.ONE
        if (tenPowerName.startsWith(persianOne)) {
            tenPowerName = tenPowerName.replace(persianOne, "").trim()
        }
        return tenPowerName
    }
    
    /**
     * this method check if input
     * is only containing numbers
     *
     * @param input is number in String type
     * @return true if only contains number characters
     */
    private fun isNumberOnly(input : String) : Boolean {
        var isNumberOnly = true
        input.forEach { char -> isNumberOnly = isNumberOnly && char in '0'..'9' }
        return isNumberOnly
    }

    companion object {
        
        /**
         * extension method to for spelling number to farsi
         *
         */
        fun Any.spell(context: Context) = Digits(context).spellToFarsi(this)
    }
}
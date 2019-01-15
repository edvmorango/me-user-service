package com.me.userservice.service


object ValidationService {


    fun isValidCpf(cpf: String): Boolean {

        when {
            cpf.length != 11 -> return false
            !cpf.map { it.isDigit() }.reduce{ a, b -> a && b } -> return false
            else -> {

                val penult = cpf.substring(9, 10).toInt()
                val last = cpf.substring(10, 11).toInt()

                val digits = cpf.take(9).toCharArray().map { it.toString().toInt() }

                val penultTot =  digits
                        .zip(10 downTo 2)
                        .fold(0){ acc, (c1, c2) -> acc + (c1 * c2)  }


                val remainderPenult = penultTot % 11
                val penultCalc = if (remainderPenult < 2) 0 else 11 - remainderPenult

                val lastTot = listOf(*digits.toTypedArray(), penultCalc)
                        .zip(11 downTo 2)
                        .fold(0){ acc, (c1, c2) -> acc + (c1 * c2)  }


                val remainderLast = lastTot % 11
                val lastCalc =  if (remainderLast < 2) 0 else 11 - remainderLast


                return (penult == penultCalc && lastCalc == last)

            }
        }


    }

    fun isValidEmail(email: String): Boolean {
        return email.matches(Regex.fromLiteral("*@*"))
    }


    fun isValidPhone(phone: String): Boolean {
        return phone.length == 13 && phone.map { it.isDigit() }.reduce{ a, b -> a && b }
    }

}
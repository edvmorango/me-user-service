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

                val penulCalc = 11 - (penultTot % 11)

                val lastTot = listOf(*digits.toTypedArray(), penulCalc)
                        .zip(11 downTo 2)
                        .fold(0){ acc, (c1, c2) -> acc + (c1 * c2)  }

                val lastCalc =  11 - (lastTot % 11)


                return (penult == penulCalc && lastCalc == last)

            }
        }


    }




}
package com.me.userservice.service


object ValidationService {


    fun validateCpf(cpf: String): Boolean {

        when {
            cpf.length != 11 -> return false
            cpf.map { it.isDigit() }.reduce{ a, b -> a && b } -> return false
            else -> {

                val penult = cpf.substring(9, 10).toInt()
                val last = cpf.substring(10, 11).toInt()

                val digits = cpf.take(9).map { it.toInt() }

                val fd = digits
                        .zip(10 downTo 2)
                        .fold(0){ acc, (c1, c2) -> acc + (c1 * c2)  } % 11

                val sd = listOf(*digits.toTypedArray(), fd)
                        .zip(11 downTo 2)
                        .fold(0){ acc, (c1, c2) -> acc + (c1 * c2)  } % 11

                return (penult == fd && sd == last)

            }
        }


    }




}
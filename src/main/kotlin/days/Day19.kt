package days

class Day19 : Day(19) {

    override fun partOne(): Any {
        // takes about 2m33 using runProgram and answers 1848
        val ans = runProgram(inputList)
        return ans.second[0]
    }

    override fun partTwo(): Any {
        // runs for a very long time if using runProgram
        // according to the subreddit turns out we were not supposed to solve this
        // in code but rather debug and and work out what it's doing
        // after doing so it looks like it is doing a sum of divisors of the
        // number in the stable register
        //val ans = runProgram(inputList, intArrayOf(1, 0, 0, 0, 0, 0))
        //return ans.second[0]
        return 22157688
    }

    data class Instruction(val op: String, val a: Int, val b: Int, val c: Int)

    companion object {

        fun runProgram(input: List<String>, registers: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)): Pair<Int, IntArray> {
            val ipReg = input.first().split(" ").last().toInt()
            var ip = 0
            val instructions = input.drop(1).map { parseInstruction(it) }

            while (ip > -1 && ip < instructions.size) {
                // System.err.print("ip=$ip ${Arrays.toString(registers)}")
                val ins = instructions[ip]
                Day16.Op.invoke(ins.op, registers, ins.a, ins.b, ins.c)
                // System.err.println(" ${ins.op} ${ins.a} ${ins.b} ${ins.c} ${Arrays.toString(registers)}")
                ip = registers[ipReg] +++ 1
            }

            return ip to registers
        }

        fun parseInstruction(input: String): Instruction {
            val instruction = input.split(" ")
            return Instruction(instruction[0], instruction[1].toInt(), instruction[2].toInt(), instruction[3].toInt())
        }
    }
}

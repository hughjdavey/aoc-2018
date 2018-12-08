package days

import util.addAndGet

class Day7 : Day(7) {

    override fun partOne(): String {
        val steps = parseSteps(inputList)

        val execution = (0 until steps.size).map { i ->
            val eligibleSteps = steps.filterNot { it.done }
                .filter { it.dependencies.isEmpty() }
                .sortedBy { it.name }

            val mostEligible = eligibleSteps.first()
            mostEligible.complete()
            steps.forEach { step -> step.dependencies.removeIf { it == mostEligible.name } }
            mostEligible.name
        }
        return execution.joinToString("")
    }

    // todo improve this (remove while loop ideally)
    override fun partTwo(): Any {
        val steps = parseSteps(inputList)
        val workerPool = Array(5) { Worker() }

        var seconds = -1
        while (true) {
            workerPool.forEach { it.update() }
            seconds++

            val completedSteps = steps.filter { it.done }.map { it.name }
            if (completedSteps.size == steps.size) {
                break
            }

            steps.forEach { step -> step.dependencies.removeIf { completedSteps.contains(it) } }

            val eligibleSteps = steps.filterNot { it.doing }
                .filter { it.dependencies.isEmpty() }
                .sortedBy { it.name }

            if (workerPool.none { it.isFree() }) {
                continue
            }
            else if (eligibleSteps.isEmpty()) {
                continue
            }

            eligibleSteps.zip(workerPool.filter { it.isFree() }).forEach { (step, worker) ->
                worker.startWork(step)
            }
            //println("Second: $seconds; Steps: ${steps.filter { it.doing }}; Done: ${steps.filter { it.done }}")
        }
        return seconds
    }

    data class Step(val name: Char) {
        val dependencies = mutableSetOf<Char>()
        var secondsNeeded = 60 + ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(this.name) + 1)

        var done = false
        var doing = false

        fun complete() {
            done = true
            doing = false
        }

        fun doWork() {
            if (--secondsNeeded == 0) {
                this.complete()
            }
        }
    }

    class Worker {
        private var step: Step? = null

        fun isFree() = step == null

        fun startWork(step: Step) {
            this.step = step
            step.doing = true
        }

        fun update() {
            if (this.step != null) {
                this.step!!.doWork()
                if (this.step!!.done) {
                    this.step = null
                }
            }
        }
    }

    companion object {

        fun parseSteps(input: List<String>): Collection<Step> {
            return input.map { str -> str[36] to str[5] }.fold(mutableSetOf()) { steps, dependency ->
                (steps.find { it.name == dependency.first } ?: steps.addAndGet(Step(dependency.first))).dependencies.add(dependency.second)
                (steps.find { it.name == dependency.second } ?: steps.addAndGet(Step(dependency.second)))
                steps
            }
        }
    }
}

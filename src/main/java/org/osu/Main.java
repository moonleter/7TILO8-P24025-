package org.osu;

import java.util.Arrays;
import java.util.List;

/**
 * "" direct operand
 * "=" constant operand
 * "*" indirect operand
 */
public class Main {
    public static void main(String[] args) {
        // Task 1 fun(n_1, n_2, ... , n_m) = Π_{i=1}^{m} n_i | n_i ∈ N^+, m > 0
        List<InstructionCommand> task1Multiply = Arrays.asList(
                new InstructionCommand(Instruction.LOAD, "=", 1),
                new InstructionCommand(Instruction.STORE, "=", 1),
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.MUL, "", 1),
                new InstructionCommand(Instruction.JZ, "", 7),
                new InstructionCommand(Instruction.STORE, "=", 1),
                new InstructionCommand(Instruction.JMP, "=", 2),
                new InstructionCommand(Instruction.WRITE, "", 1),
                new InstructionCommand(Instruction.HALT, "=", 0)
        );
        Tape inputTape1 = new Tape(0, Arrays.asList(2, 3, 4, 0));
        int additionalRegistries1 = 4;
        RAM RAM1 = new RAM(inputTape1, task1Multiply, additionalRegistries1);
        RAM1.run();

        System.out.println("\n");
        System.out.println("______________________________________________________________");

        // Task 2  {1^n 2^n 3^n | n >= 0}
        List<InstructionCommand> task2 = Arrays.asList(
                new InstructionCommand(Instruction.READ, "", 0),
                new InstructionCommand(Instruction.JZ, "", 23),
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 8),
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 13),
                new InstructionCommand(Instruction.SUB, "=", 1),
                new InstructionCommand(Instruction.JZ, "", 18),
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 2),
                new InstructionCommand(Instruction.MUL, "=", 0),
                new InstructionCommand(Instruction.JZ, "", 0),
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 3),
                new InstructionCommand(Instruction.MUL, "=", 0),
                new InstructionCommand(Instruction.JZ, "", 0),
                new InstructionCommand(Instruction.LOAD, "", 4),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.STORE, "", 4),
                new InstructionCommand(Instruction.MUL, "=", 0),
                new InstructionCommand(Instruction.JZ, "", 0),
                new InstructionCommand(Instruction.LOAD, "", 2),
                new InstructionCommand(Instruction.SUB, "", 3),
                new InstructionCommand(Instruction.JGTZ, "", 33),
                new InstructionCommand(Instruction.LOAD, "", 3),
                new InstructionCommand(Instruction.SUB, "", 4),
                new InstructionCommand(Instruction.JGTZ, "", 33),
                new InstructionCommand(Instruction.MUL, "=", 0),
                new InstructionCommand(Instruction.ADD, "=", 1),
                new InstructionCommand(Instruction.WRITE, "", 0),
                new InstructionCommand(Instruction.HALT, "", 0),
                new InstructionCommand(Instruction.MUL, "=", 0),
                new InstructionCommand(Instruction.WRITE, "", 0),
                new InstructionCommand(Instruction.HALT, "", 0)
        );
        Tape inputTape2 = new Tape(0, Arrays.asList(1, 1, 2, 2, 3, 3, 0));
        int additionalRegistries2 = 10;
        RAM RAM2 = new RAM(inputTape2, task2, additionalRegistries2);
        RAM2.run();
    }
}
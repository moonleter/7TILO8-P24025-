package org.osu;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    Tape inputTape;
    Tape outputTape;
    List<Integer> register;
    List<InstructionCommand> instructionCommands;

    public RAM(Tape inputTape, List<InstructionCommand> instructionCommands, int additionalRegistries) {
        this.inputTape = inputTape;
        this.instructionCommands = instructionCommands;
        this.outputTape = new Tape(0, new ArrayList<>());
        this.register = new ArrayList<>(List.of(0));
        for (int i = 0; i < additionalRegistries; i++) {
            register.add(0);
        }
    }

    int initialCommandIndex = 0;

    public void run() {
        boolean isRunning = true;

        while (isRunning) {
            InstructionCommand c = instructionCommands.get(initialCommandIndex);
            switch (c.instruction) {
                case READ -> read(c.value);
                case WRITE -> write(c);
                case LOAD -> load(c);
                case STORE -> store(c);
                case ADD -> add(c);
                case SUB -> sub(c);
                case MUL -> mul(c);
                case DIV -> div(c);
                case JMP -> jump(c.value);
                case JZ -> jumpZero(c.value);
                case JGTZ -> jumpGreaterThanZero(c.value);
                case HALT -> isRunning = false;
                default -> throw new RuntimeException("Unknown command");
            }

            c.print();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < register.size(); i++) {
                builder.append(register.get(i));
                if (i < register.size() - 1) {
                    builder.append(",");
                }
            }
            System.out.print("Registr: " + builder + " ");

            System.out.println("IN head position: " + inputTape.head);

            initialCommandIndex++;
        }

        System.out.print("\nOutput tape: ");
        outputTape.content.forEach(c -> System.out.print(c + " "));
    }

    private void read(int value) {
        register.set(value, inputTape.content.get(inputTape.head));
        inputTape.head++;
    }

    private void write(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            outputTape.content.add(register.get(instructionCommand.value));
        if (instructionCommand.operand != null && instructionCommand.operand.equals("="))
            outputTape.content.add(instructionCommand.value);
        if (instructionCommand.operand != null && instructionCommand.operand.equals("*"))
            outputTape.content.add(register.get(register.get(0) + instructionCommand.value));
        outputTape.head++;
    }

    private void load(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            register.set(0, register.get(instructionCommand.value));
        if (instructionCommand.operand != null && instructionCommand.operand.equals("="))
            register.set(0, instructionCommand.value);
        if (instructionCommand.operand != null && instructionCommand.operand.equals("*"))
            register.set(0, register.get(register.get(0) + instructionCommand.value));
    }

    private void store(InstructionCommand instructionCommand) {
        register.set(instructionCommand.value, register.get(0));
    }

    private void add(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            register.set(0, register.get(0) + register.get(instructionCommand.value));
        if (instructionCommand.operand != null && instructionCommand.operand.equals("="))
            register.set(0, register.get(0) + instructionCommand.value);
        if (instructionCommand.operand != null && instructionCommand.operand.equals("*"))
            register.set(0, register.get(0) + register.get(register.get(0) + instructionCommand.value));
    }

    private void sub(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            register.set(0, register.get(0) - register.get(instructionCommand.value));
    }

    private void mul(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            register.set(0, register.get(0) * register.get(instructionCommand.value));
        if (instructionCommand.operand != null && instructionCommand.operand.equals("="))
            register.set(0, register.get(0) * instructionCommand.value);
        if (instructionCommand.operand != null && instructionCommand.operand.equals("*"))
            register.set(0, register.get(0) * register.get(register.get(0) + instructionCommand.value));
    }

    private void div(InstructionCommand instructionCommand) {
        if (instructionCommand.operand == null)
            register.set(0, register.get(0) / register.get(instructionCommand.value));
        if (instructionCommand.operand != null && instructionCommand.operand.equals("="))
            register.set(0, register.get(0) / instructionCommand.value);
        if (instructionCommand.operand != null && instructionCommand.operand.equals("*"))
            register.set(0, register.get(0) / register.get(register.get(0) + instructionCommand.value));
    }

    private void jump(int value) {
        initialCommandIndex = value - 1;
    }

    private void jumpZero(int value) {
        if (register.get(0) == 0)
            initialCommandIndex = value - 1;
    }

    private void jumpGreaterThanZero(int value) {
        if (register.get(0) > 0)
            initialCommandIndex = value - 1;
    }
}

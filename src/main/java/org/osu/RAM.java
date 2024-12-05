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
                case READ -> read(c.value, c.operand);
                case WRITE -> write(c, c.operand);
                case LOAD -> load(c, c.operand);
                case STORE -> store(c, c.operand);
                case ADD -> add(c, c.operand);
                case SUB -> sub(c, c.operand);
                case MUL -> mul(c);
                case DIV -> div(c, c.operand);
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

    private void read(int value, String operand) {
        if (operand == null) {
            register.set(value, inputTape.content.get(inputTape.head));
        }
        inputTape.head++;
    }

    private void write(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            outputTape.content.add(register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            outputTape.content.add(instructionCommand.value);
        } else if (operand.equals("*")) {
            outputTape.content.add(register.get(register.get(0) + instructionCommand.value));
        }
        outputTape.head++;
    }

    private void load(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            register.set(0, register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            register.set(0, instructionCommand.value);
        } else if (operand.equals("*")) {
            register.set(0, inputTape.content.get(inputTape.head + instructionCommand.value));
        }
    }

    private void store(InstructionCommand instructionCommand, String operand) {
        if (operand == null || operand.equals("")) {
            register.set(instructionCommand.value, register.get(0));
        } else if (operand.equals("*")) {
            int address = register.get(instructionCommand.value + register.get(1));
            register.set(address, register.get(0));
        } else if (operand.equals("=")) {
            register.set(instructionCommand.value, register.get(0));
        } else {
            throw new IllegalArgumentException("Invalid operand for STORE instruction");
        }
    }

    private void add(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            register.set(0, register.get(0) + register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            register.set(0, register.get(0) + instructionCommand.value);
        } else if (operand.equals("*")) {
            register.set(0, register.get(0) + inputTape.content.get(register.get(0) + instructionCommand.value));
        }
    }

    private void sub(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            register.set(0, register.get(0) - register.get(instructionCommand.value));
        } else if (operand.equals("*")) {
            register.set(0, register.get(0) - inputTape.content.get(register.get(0) + instructionCommand.value));
        }
    }

    private void mul(InstructionCommand instructionCommand) {
        if (instructionCommand.operand.equals("")) {
            register.set(0, register.get(0) * register.get(instructionCommand.value));
        } else if (instructionCommand.operand.equals("=")) {
            register.set(0, register.get(0) * instructionCommand.value);
        } else if (instructionCommand.operand.equals("*")) {
            int address = inputTape.head - 1; // Use the current tape position
            if (address >= 0 && address < inputTape.content.size()) {
                register.set(0, register.get(0) * inputTape.content.get(address));
            } else {
                throw new RuntimeException("Invalid indirect address: " + address);
            }
        }
    }


    private void div(InstructionCommand instructionCommand, String operand) {
        if (operand == null) {
            register.set(0, register.get(0) / register.get(instructionCommand.value));
        } else if (operand.equals("=")) {
            register.set(0, register.get(0) / instructionCommand.value);
        } else if (operand.equals("*")) {
            register.set(0, register.get(0) / inputTape.content.get(register.get(0) + instructionCommand.value));
        }
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
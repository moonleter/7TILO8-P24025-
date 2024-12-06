package org.osu;

/**
 * DIV and JH are not implemented because they are not used in the tasks.
 */
public enum Instruction {
    READ,
    WRITE,
    LOAD,
    STORE,
    ADD,
    SUB,
    MUL,
    JMP,
    JZ,
    JGTZ,
    HALT
}
package me.tryfle.stormy.hooks;

import net.weavemc.loader.api.Hook;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class CPSHook extends Hook {
    public static int leftCps = 0;
    public static int rightCps = 0;
    public static String CPS_ClASS_NAME = "com/moonsworth/lunar/client/HCRICIICRCIHCHIRHIIICIHCR/OHCOOHIIHCCCCCORHIRIRCIOO/RCHRHRRRRRCROOHROOIRRCORH",
            LEFT_CPS_METHOD_NAME = "RHORCRICRIIRHOIHOCRRORHCC",
            RIGHT_CPS_METHOD_NAME = "RHORCRICRIIRHOIHOCRRORHCC", CURRENT_HOOK_CLASS_PATH = "me/tryfle/stormy/hooks/CPSHook";
    public CPSHook() {
        super(CPS_ClASS_NAME);
    }

    @Override
    public void transform(@NotNull ClassNode classNode, @NotNull AssemblerConfig assemblerConfig) {
        MethodNode methodNode = classNode.methods.stream()
                .filter(m -> m.name.equals(LEFT_CPS_METHOD_NAME))
                .findFirst()
                .orElseThrow();

        InsnList instructions = methodNode.instructions;
        AbstractInsnNode currentInsn = instructions.getFirst();

        while (currentInsn != null) {
            if (currentInsn.getOpcode() == Opcodes.IRETURN) {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, CURRENT_HOOK_CLASS_PATH, "leftCps", "I"));
                newInstructions.add(new InsnNode(Opcodes.IADD));
                newInstructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, CURRENT_HOOK_CLASS_PATH, "leftCps", "I"));

                instructions.insertBefore(currentInsn, newInstructions);
            }

            currentInsn = currentInsn.getNext();
        }

        methodNode = classNode.methods.stream()
                .filter(m -> m.name.equals(RIGHT_CPS_METHOD_NAME))
                .findFirst()
                .orElseThrow();

        instructions = methodNode.instructions;
        currentInsn = instructions.getFirst();

        while (currentInsn != null) {
            if (currentInsn.getOpcode() == Opcodes.IRETURN) {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, CURRENT_HOOK_CLASS_PATH, "rightCps", "I"));
                newInstructions.add(new InsnNode(Opcodes.IADD));
                newInstructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, CURRENT_HOOK_CLASS_PATH, "rightCps", "I"));
                instructions.insertBefore(currentInsn, newInstructions);
            }

            currentInsn = currentInsn.getNext();
        }
    }
    /**
     * Adds a left click onto the CPS mod.
     */
    public static void leftClick() {
        new Thread(() -> {
            try {
                leftCps += 1;

                Thread.sleep(1000);

                leftCps -= 1;
            } catch (final Exception ignored) { }
        }).start();
    }

    /**
     * Adds a right click onto the CPS mod.
     */
    public static void rightClick() {
        new Thread(() -> {
            try {
                rightCps += 1;

                Thread.sleep(1000);

                rightCps -= 1;
            } catch (final Exception ignored) { }
        }).start();
    }
}
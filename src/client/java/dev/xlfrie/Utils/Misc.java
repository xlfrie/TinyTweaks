package dev.xlfrie.Utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class Misc {
    private static List<Vec3d> blocksInRange;
    private static List<BlockPos> blocksInRangeCache;
    private static BlockPos lastBlocksInRangePos;

    public static <T> T getPrivate(Object obj, String mappedFieldName, String intermediaryFieldName) {
        Class<?> clazz = obj.getClass();
        Field field;

        try {
            field = clazz.getField(mappedFieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = clazz.getField(intermediaryFieldName);
            } catch (NoSuchFieldException ex) {
                return null;
            }
        }
        field.setAccessible(true);

        try {
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeFunction(Object obj, String mappedFuncName, String intermediaryFuncName) {
        Class<?> clazz = obj.getClass();
        Method method;
        try {
            method = clazz.getDeclaredMethod(mappedFuncName);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getDeclaredMethod(intermediaryFuncName);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
        method.setAccessible(true);

        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean inRange(BlockPos blockPos, BlockPos playerPos) {
        return Math.pow(playerPos.getX() - blockPos.getX(), 2) + Math.pow(playerPos.getY() - blockPos.getY(), 2) + Math.pow(playerPos.getZ() - blockPos.getZ(), 2) <= Math.pow(clientPlayerInteractionManager.getReachDistance(), 2);
    }

    public static List<BlockPos> getBlocksInRange(BlockPos pos) {
        float range = clientPlayerInteractionManager.getReachDistance();
        if (blocksInRange == null) {
            blocksInRange = new ArrayList<>();
            for (float y = -range; y < range; y++) {
                for (float x = -range; x < range; x++) {
                    for (float z = -range; z < range; z++) {
                        if (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) <= Math.pow(range, 2)) {
                            blocksInRange.add(new Vec3d(x, y, z));
                        }
                    }
                }
            }
        }

        if (blocksInRangeCache != null && lastBlocksInRangePos.equals(pos)) {
            return blocksInRangeCache;
        } else {
            List<BlockPos> cache = new ArrayList<>();


            for (Iterator<Vec3d> it = blocksInRange.iterator(); it.hasNext(); ) {
                Vec3d vec3d = it.next();
                cache.add(new BlockPos((int) (vec3d.x + pos.getX()), (int) (vec3d.y + pos.getY()), (int) (vec3d.z + pos.getZ())));
            }

            blocksInRangeCache = cache;
            lastBlocksInRangePos = pos;

            return cache;
        }
    }
}

Single block display
```java

var spawnPos = new BlockPos(42, 0, 69);
var customTag = new CompoundTag();
SingleBlockDisplayHandle singleBlockHandle = new SingleBlockDisplayBuilder(spawnPos)
        .withBlock(Blocks.CYAN_STAINED_GLASS)
        .glowColor(ChatFormatting.RED)
        .lightLevel(15)
        .customData("modid:data", customTag) // custom data to identify or filter DisplayBlock Entities later 
        .build(level);
```


BlockDisplay for a given Shape (currently available: Cuboid and Sphere)
```java

var a = new BlockPos(0, 0, 0);
var b = new BlockPos(6, 6, 6);
CuboidShape shape = new CuboidShape(a, b);

ShapedBlockDisplay shapedHandle = new ShapedBlockDisplayBuilder(shape)
            .withBlock(Blocks.RED_STAINED_GLASS) // random stained glass by default
            .glowing(true) // false by default
            .glowColor(ChatFormatting.AQUA) // white by default
            .lightLevel(12) // 15 by default
            .initialStyle(ShapeStyle.FRAME) // FRAME by default
            .build(level);

```


You can update the properties of those entities using the handles:

```java
SingleBlockDisplayHandle singleBlockHandle;
singleBlockHandle.updateBlock(Blocks.WHITE_STAINED_GLASS.defaultBlockState());
singleBlockHandle.updateGlow(true);
singleBlockHandle.updateGlowColor(ChatFormatting.RED);
singleBlockHandle.updateLightLevel(15);
// and discard the entity by using
singleBlockHandle.remove();


```

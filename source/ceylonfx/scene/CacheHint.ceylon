import javafx.scene{ JCacheHint=CacheHint }

"Cache hints for use with `Node.cacheHint`"
shared abstract class CacheHint(shared JCacheHint delegate)
		of defaultCacheHint|qualityCacheHint|rotateCacheHint|scaleCacheHint|
		scaleAndRotateCacheHint|speedCacheHint {}

"No additional hint."
shared object defaultCacheHint extends CacheHint(JCacheHint.\iDEFAULT) {}

"A hint to tell the bitmap caching mechanism that this node should appear on screen at the highest visual quality."
shared object qualityCacheHint extends CacheHint(JCacheHint.\iQUALITY) {}

"A hint to tell the bitmap caching mechanism that if the node is rotated, it is acceptable to paint it by rotating the the cached bitmap (rather than re-rendering the node)."
shared object rotateCacheHint extends CacheHint(JCacheHint.\iROTATE) {}

"A hint to tell the bitmap caching mechanism that if the node is scaled up or down, it is acceptable to paint it by scaling the cached bitmap (rather than re-rendering the node)."
shared object scaleCacheHint extends CacheHint(JCacheHint.\iSCALE) {}

"A hint to tell the bitmap caching mechanism that if the node is scaled and/or rotated, it is acceptable to paint it by scaling and/or rotating the cached bitmap (rather than re-rendering the node)."
shared object scaleAndRotateCacheHint extends CacheHint(JCacheHint.\iSCALE_AND_ROTATE) {}

"A hint to tell the bitmap caching mechanism that this node is animating, and should be painted from the bitmap cache whenever possible in order to maintain smooth animation."
shared object speedCacheHint extends CacheHint(JCacheHint.\iSPEED) {}

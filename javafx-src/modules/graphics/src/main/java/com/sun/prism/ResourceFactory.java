/*
 * Copyright (c) 2009, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.prism;

import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.shape.ShapeRep;

public interface ResourceFactory extends GraphicsResource {

    /**
     * Returns status of this graphics device.
     * If the device is not ready the createRTTexture and
     * present operations will fail.
     * Creation of shaders and regular textures will succeed and
     * return valid resources.
     * All hardware resources (RenderTargets and SwapChains) have to be recreated
     * after a device-lost event notification.
     **/

    public boolean isDeviceReady();

    public TextureResourcePool getTextureResourcePool();

    /**
     * Returns a new {@code Texture} containing the pixels from the given
     * image with the indicated texture edge wrap mode.
     * Note that the dimensions of the returned texture may be larger
     * than those of the given image.
     * <p>
     * Equivalent to (but perhaps more efficient than):
     * <pre><code>
     *     PixelFormat format = image.getPixelFormat();
     *     int w = image.getWidth();
     *     int h = image.getHeight();
     *     Texture tex = createTexture(format, usageHint, wrapMode, w, h);
     *     tex.update(image, 0, 0, w, h);
     * </code></pre>
     *
     * @param image the pixel data to be uploaded to the new texture
     * @param usageHint the Dynamic vs. Static nature of the texture data
     * @param wrapMode the desired edge behavior (clamping vs. wrapping)
     * @return a new texture
     */
    public Texture createTexture(Image image,
                                 Texture.Usage usageHint,
                                 Texture.WrapMode wrapMode);

    /**
     * Returns a new {@code Texture} with the given format and edge wrapping
     * support.  Note that the dimensions of the returned texture may be larger
     * than those requested and the wrap mode may be a simulated version of
     * the type requested.
     *
     * @param formatHint intended pixel format of the data to be stored
     *     in this texture
     * @param wrapMode intended wrap mode to be used for the texture
     * @param w width of the content in the texture
     * @param h height of the content in the texture
     * @return texture most appropriate for the given intended format, wrap
     * mode and dimensions
     */
    public Texture createTexture(PixelFormat formatHint,
                                 Texture.Usage usageHint,
                                 Texture.WrapMode wrapMode,
                                 int w, int h);

    /**
     * Returns a new {@code Texture} that can contain the video image as specified
     * in the provided {@code MediaFrame}. Note that padding is almost implicit
     * since this method has to accommodate the line strides of each plane. Also
     * due to renderer limitations, some format conversion may be necessary so
     * the texture format may end up being different from the video image format.
     *
     * @param frame the video image that we need to create a new texture for
     * @return texture most appropriate for the given video image.
     */
    public Texture createTexture(MediaFrame frame);

    /**
     * Returns a {@code Texture} for the given image set up to use or
     * simulate the indicated wrap mode.
     * If no texture could be found in the cache, this method will create a
     * new texture and put it in the cache before returning it.
     * NOTE: the caller of this method should not hold a reference to the
     * cached texture beyond its immediate needs since the cache may be
     * cleared at any time.
     *
     * @param image the pixel data to be uploaded if the texture is new or
     *     needs new fringe pixels to simulate a new wrap mode
     * @param wrapMode the mode that describes the behavior for samples
     *     outside the content area
     * @return a cached texture
     */
    public Texture getCachedTexture(Image image, Texture.WrapMode wrapMode);

    /**
     * Returns true if the given {@code PixelFormat} is supported; otherwise
     * returns false.
     * <p>
     * Note that the following formats are guaranteed to be supported
     * across all devices:
     * <pre><code>
     *     BYTE_RGB
     *     BYTE_RGBA_PRE
     *     BYTE_GRAY
     *     BYTE_ALPHA
     * </code></pre>
     * <p>
     * Support for the other formats depends on the capabilities of the
     * device.  Be sure to call this method before attempting to create
     * a {@code Texture} with a non-standard format and plan to have an
     * alternate codepath if the given format is not supported.
     *
     * @param format the {@code PixelFormat} to test
     * @return true if the given format is supported; false otherwise
     */
    public boolean isFormatSupported(PixelFormat format);

    /**
     * Returns the maximum supported texture dimension for this device.
     * For example, if this method returns 2048, it means that textures
     * larger than 2048x2048 cannot be created.
     *
     * @return the maximum supported texture dimension
     */
    public int getMaximumTextureSize();

    public int getRTTWidth(int w, Texture.WrapMode wrapMode);
    public int getRTTHeight(int h, Texture.WrapMode wrapMode);

    public Texture createMaskTexture(int width, int height, Texture.WrapMode wrapMode);
    public Texture createFloatTexture(int width, int height);
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode);
    public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode, boolean antiAliasing);
    
    /**
     * A Texture may have been obtained from a different resource factory.
     * @param tex the texture to check.
     * @return whether this texture is compatible.
     */
    public boolean isCompatibleTexture(Texture tex);

    public Presentable createPresentable(PresentableState pState);
    public VertexBuffer createVertexBuffer(int maxQuads);

    public ShapeRep createPathRep();
    public ShapeRep createRoundRectRep();
    public ShapeRep createEllipseRep();
    public ShapeRep createArcRep();

    public void addFactoryListener(ResourceFactoryListener l);
    public void removeFactoryListener(ResourceFactoryListener l);

    public void setRegionTexture(Texture texture);
    public Texture getRegionTexture();
    public void setGlyphTexture(Texture texture);
    public Texture getGlyphTexture();
    public boolean isSuperShaderAllowed();

    public void dispose();

    /*
     * 3D stuff
     */
    public PhongMaterial createPhongMaterial();
    public MeshView createMeshView(Mesh mesh);
    public Mesh createMesh();
}

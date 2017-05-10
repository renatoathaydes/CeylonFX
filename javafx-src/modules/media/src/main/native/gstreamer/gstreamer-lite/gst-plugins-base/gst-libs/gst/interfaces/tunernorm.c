/* GStreamer Tuner
 * Copyright (C) 2003 Ronald Bultje <rbultje@ronald.bitfreak.net>
 *
 * tunernorm.c: tuner norm object design
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "tunernorm.h"

/**
 * SECTION:gsttunernorm
 * @short_description: Encapsulates information about the data format(s)
 * for a #GstTunerChannel.
 *
 * <refsect2>
 * <para>The #GstTunerNorm object is created by an element implementing the
 * #GstTuner interface and encapsulates the selection of a capture/output format
 * for a selected #GstTunerChannel.
 * </para>
 * </refsect2>
 */

enum
{
  /* FILL ME */
  LAST_SIGNAL
};

static void gst_tuner_norm_class_init (GstTunerNormClass * klass);
static void gst_tuner_norm_init (GstTunerNorm * norm);
static void gst_tuner_norm_dispose (GObject * object);

static GObjectClass *parent_class = NULL;

/*static guint signals[LAST_SIGNAL] = { 0 };*/

GType
gst_tuner_norm_get_type (void)
{
  static GType gst_tuner_norm_type = 0;

  if (!gst_tuner_norm_type) {
    static const GTypeInfo tuner_norm_info = {
      sizeof (GstTunerNormClass),
      NULL,
      NULL,
      (GClassInitFunc) gst_tuner_norm_class_init,
      NULL,
      NULL,
      sizeof (GstTunerNorm),
      0,
      (GInstanceInitFunc) gst_tuner_norm_init,
      NULL
    };

    gst_tuner_norm_type =
        g_type_register_static (G_TYPE_OBJECT,
        "GstTunerNorm", &tuner_norm_info, 0);
  }

  return gst_tuner_norm_type;
}

static void
gst_tuner_norm_class_init (GstTunerNormClass * klass)
{
  GObjectClass *object_klass = (GObjectClass *) klass;

  parent_class = g_type_class_peek_parent (klass);

  object_klass->dispose = gst_tuner_norm_dispose;
}

static void
gst_tuner_norm_init (GstTunerNorm * norm)
{
  norm->label = NULL;
  g_value_init (&norm->framerate, GST_TYPE_FRACTION);
}

static void
gst_tuner_norm_dispose (GObject * object)
{
  GstTunerNorm *norm = GST_TUNER_NORM (object);

  if (norm->label) {
    g_free (norm->label);
    norm->label = NULL;
  }

  if (parent_class->dispose)
    parent_class->dispose (object);
}

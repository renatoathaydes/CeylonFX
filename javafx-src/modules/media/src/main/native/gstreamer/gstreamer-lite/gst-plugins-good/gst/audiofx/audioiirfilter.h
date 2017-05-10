/* 
 * GStreamer
 * Copyright (C) 2009 Sebastian Dröge <sebastian.droege@collabora.co.uk>
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
 * 
 */

#ifndef __GST_AUDIO_IIR_FILTER_H__
#define __GST_AUDIO_IIR_FILTER_H__

#include <gst/gst.h>
#include <gst/audio/gstaudiofilter.h>

#include "audiofxbaseiirfilter.h"

G_BEGIN_DECLS

#define GST_TYPE_AUDIO_IIR_FILTER \
  (gst_audio_iir_filter_get_type())
#define GST_AUDIO_IIR_FILTER(obj) \
  (G_TYPE_CHECK_INSTANCE_CAST((obj),GST_TYPE_AUDIO_IIR_FILTER,GstAudioIIRFilter))
#define GST_AUDIO_IIR_FILTER_CLASS(klass) \
  (G_TYPE_CHECK_CLASS_CAST((klass),GST_TYPE_AUDIO_IIR_FILTER,GstAudioIIRFilterClass))
#define GST_IS_AUDIO_IIR_FILTER(obj) \
  (G_TYPE_CHECK_INSTANCE_TYPE((obj),GST_TYPE_AUDIO_IIR_FILTER))
#define GST_IS_AUDIO_IIR_FILTER_CLASS(klass) \
  (G_TYPE_CHECK_CLASS_TYPE((klass),GST_TYPE_AUDIO_IIR_FILTER))

typedef struct _GstAudioIIRFilter GstAudioIIRFilter;
typedef struct _GstAudioIIRFilterClass GstAudioIIRFilterClass;

/**
 * GstAudioIIRFilter:
 *
 * Opaque data structure.
 */
struct _GstAudioIIRFilter {
  GstAudioFXBaseIIRFilter parent;

  GValueArray *a, *b;

  /* < private > */
  GMutex *lock;
  gint rate;
};

struct _GstAudioIIRFilterClass {
  GstAudioFXBaseIIRFilterClass parent;

  void (*rate_changed) (GstElement * element, gint rate);
};

GType gst_audio_iir_filter_get_type (void);

G_END_DECLS

#endif /* __GST_AUDIO_IIR_FILTER_H__ */

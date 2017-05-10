/* GStreamer
 * Copyright (C) <1999> Erik Walthinsen <omega@cse.ogi.edu>
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
 /*
 * Unless otherwise indicated, Source Code is licensed under MIT license.
 * See further explanation attached in License Statement (distributed in the file
 * LICENSE).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


#ifndef __FOURCC_H__
#define __FOURCC_H__

#include <gst/gst.h>

G_BEGIN_DECLS

#define FOURCC_null     0x0

#define FOURCC_moov     GST_MAKE_FOURCC('m','o','o','v')
#define FOURCC_mvhd     GST_MAKE_FOURCC('m','v','h','d')
#define FOURCC_clip     GST_MAKE_FOURCC('c','l','i','p')
#define FOURCC_trak     GST_MAKE_FOURCC('t','r','a','k')
#define FOURCC_udta     GST_MAKE_FOURCC('u','d','t','a')
#define FOURCC_ctab     GST_MAKE_FOURCC('c','t','a','b')
#define FOURCC_tkhd     GST_MAKE_FOURCC('t','k','h','d')
#define FOURCC_crgn     GST_MAKE_FOURCC('c','r','g','n')
#define FOURCC_matt     GST_MAKE_FOURCC('m','a','t','t')
#define FOURCC_kmat     GST_MAKE_FOURCC('k','m','a','t')
#define FOURCC_edts     GST_MAKE_FOURCC('e','d','t','s')
#define FOURCC_elst     GST_MAKE_FOURCC('e','l','s','t')
#define FOURCC_load     GST_MAKE_FOURCC('l','o','a','d')
#define FOURCC_tref     GST_MAKE_FOURCC('t','r','e','f')
#define FOURCC_imap     GST_MAKE_FOURCC('i','m','a','p')
#define FOURCC___in     GST_MAKE_FOURCC(' ',' ','i','n')
#define FOURCC___ty     GST_MAKE_FOURCC(' ',' ','t','y')
#define FOURCC_mdia     GST_MAKE_FOURCC('m','d','i','a')
#define FOURCC_mdhd     GST_MAKE_FOURCC('m','d','h','d')
#define FOURCC_hdlr     GST_MAKE_FOURCC('h','d','l','r')
#define FOURCC_dhlr     GST_MAKE_FOURCC('d','h','l','r')
#define FOURCC_mhlr     GST_MAKE_FOURCC('m','h','l','r')
#define FOURCC_minf     GST_MAKE_FOURCC('m','i','n','f')
#define FOURCC_mdir     GST_MAKE_FOURCC('m','d','i','r')
#define FOURCC_vmhd     GST_MAKE_FOURCC('v','m','h','d')
#define FOURCC_smhd     GST_MAKE_FOURCC('s','m','h','d')
#define FOURCC_gmhd     GST_MAKE_FOURCC('g','m','h','d')
#define FOURCC_hmhd     GST_MAKE_FOURCC('h','m','h','d')
#define FOURCC_gmin     GST_MAKE_FOURCC('g','m','i','n')
#define FOURCC_dinf     GST_MAKE_FOURCC('d','i','n','f')
#define FOURCC_dref     GST_MAKE_FOURCC('d','r','e','f')
#define FOURCC_stbl     GST_MAKE_FOURCC('s','t','b','l')
#define FOURCC_stsd     GST_MAKE_FOURCC('s','t','s','d')
#define FOURCC_stts     GST_MAKE_FOURCC('s','t','t','s')
#define FOURCC_stss     GST_MAKE_FOURCC('s','t','s','s')
#define FOURCC_stsc     GST_MAKE_FOURCC('s','t','s','c')
#define FOURCC_stsz     GST_MAKE_FOURCC('s','t','s','z')
#define FOURCC_stco     GST_MAKE_FOURCC('s','t','c','o')
#define FOURCC_vide     GST_MAKE_FOURCC('v','i','d','e')
#define FOURCC_soun     GST_MAKE_FOURCC('s','o','u','n')
#define FOURCC_strm     GST_MAKE_FOURCC('s','t','r','m')
#define FOURCC_rtsp     GST_MAKE_FOURCC('r','t','s','p')
#define FOURCC_co64     GST_MAKE_FOURCC('c','o','6','4')
#define FOURCC_cmov     GST_MAKE_FOURCC('c','m','o','v')
#define FOURCC_dcom     GST_MAKE_FOURCC('d','c','o','m')
#define FOURCC_cmvd     GST_MAKE_FOURCC('c','m','v','d')
#define FOURCC_hint     GST_MAKE_FOURCC('h','i','n','t')
#define FOURCC_mp4a     GST_MAKE_FOURCC('m','p','4','a')
#define FOURCC__mp3     GST_MAKE_FOURCC('.','m','p','3')
#define FOURCC_mp4s	GST_MAKE_FOURCC('m','p','4','s')
#define FOURCC_mp4v     GST_MAKE_FOURCC('m','p','4','v')
#define FOURCC_2vuy     GST_MAKE_FOURCC('2','v','u','y')
#define FOURCC_wave     GST_MAKE_FOURCC('w','a','v','e')
#define FOURCC_appl     GST_MAKE_FOURCC('a','p','p','l')
#define FOURCC_esds     GST_MAKE_FOURCC('e','s','d','s')
#define FOURCC_pasp     GST_MAKE_FOURCC('p','a','s','p')
#define FOURCC_hnti     GST_MAKE_FOURCC('h','n','t','i')
#define FOURCC_rtp_     GST_MAKE_FOURCC('r','t','p',' ')
#define FOURCC_sdp_     GST_MAKE_FOURCC('s','d','p',' ')
#define FOURCC_meta     GST_MAKE_FOURCC('m','e','t','a')
#define FOURCC_ilst     GST_MAKE_FOURCC('i','l','s','t')
#define FOURCC__nam     GST_MAKE_FOURCC(0xa9,'n','a','m')
#define FOURCC__ART     GST_MAKE_FOURCC(0xa9,'A','R','T')
#define FOURCC_aART     GST_MAKE_FOURCC('a','A','R','T')
#define FOURCC__wrt     GST_MAKE_FOURCC(0xa9,'w','r','t')
#define FOURCC__grp     GST_MAKE_FOURCC(0xa9,'g','r','p')
#define FOURCC__alb     GST_MAKE_FOURCC(0xa9,'a','l','b')
#define FOURCC__day     GST_MAKE_FOURCC(0xa9,'d','a','y')
#define FOURCC__des     GST_MAKE_FOURCC(0xa9,'d','e','s')
#define FOURCC__lyr     GST_MAKE_FOURCC(0xa9,'l','y','r')
#define FOURCC_gnre     GST_MAKE_FOURCC('g','n','r','e')
#define FOURCC_disc     GST_MAKE_FOURCC('d','i','s','c')
#define FOURCC_disk     GST_MAKE_FOURCC('d','i','s','k')
#define FOURCC_trkn     GST_MAKE_FOURCC('t','r','k','n')
#define FOURCC_cprt     GST_MAKE_FOURCC('c','p','r','t')
#define FOURCC_covr     GST_MAKE_FOURCC('c','o','v','r')
#define FOURCC_cpil     GST_MAKE_FOURCC('c','p','i','l')
#define FOURCC_tmpo     GST_MAKE_FOURCC('t','m','p','o')
#define FOURCC__too     GST_MAKE_FOURCC(0xa9,'t','o','o')
#define FOURCC_keyw     GST_MAKE_FOURCC('k','e','y','w')
#define FOURCC_____     GST_MAKE_FOURCC('-','-','-','-')
#define FOURCC_free     GST_MAKE_FOURCC('f','r','e','e')
#define FOURCC_data     GST_MAKE_FOURCC('d','a','t','a')
#define FOURCC_SVQ3     GST_MAKE_FOURCC('S','V','Q','3')
#define FOURCC_rmra     GST_MAKE_FOURCC('r','m','r','a')
#define FOURCC_rmda     GST_MAKE_FOURCC('r','m','d','a')
#define FOURCC_rdrf     GST_MAKE_FOURCC('r','d','r','f')
#define FOURCC__gen     GST_MAKE_FOURCC(0xa9, 'g', 'e', 'n')
#define FOURCC_rmdr     GST_MAKE_FOURCC('r','m','d','r')
#define FOURCC_rmvc     GST_MAKE_FOURCC('r','m','v','c')
#define FOURCC_qtim     GST_MAKE_FOURCC('q','t','i','m')
#define FOURCC_drms     GST_MAKE_FOURCC('d','r','m','s')
#define FOURCC_avc1     GST_MAKE_FOURCC('a','v','c','1')
#define FOURCC_h263     GST_MAKE_FOURCC('h','2','6','3')
#define FOURCC_s263     GST_MAKE_FOURCC('s','2','6','3')
#define FOURCC_avcC     GST_MAKE_FOURCC('a','v','c','C')
#define FOURCC_VP31     GST_MAKE_FOURCC('V','P','3','1')
#define FOURCC_VP80     GST_MAKE_FOURCC('V','P','8','0')
#define FOURCC_rle_     GST_MAKE_FOURCC('r','l','e',' ')
#define FOURCC_MAC6     GST_MAKE_FOURCC('M','A','C','6')
#define FOURCC_MAC3     GST_MAKE_FOURCC('M','A','C','3')
#define FOURCC_ima4     GST_MAKE_FOURCC('i','m','a','4')
#define FOURCC_ulaw     GST_MAKE_FOURCC('u','l','a','w')
#define FOURCC_alaw     GST_MAKE_FOURCC('a','l','a','w')
#define FOURCC_twos     GST_MAKE_FOURCC('t','w','o','s')
#define FOURCC_sowt     GST_MAKE_FOURCC('s','o','w','t')
#define FOURCC_raw_     GST_MAKE_FOURCC('r','a','w',' ')
#define FOURCC_QDM2     GST_MAKE_FOURCC('Q','D','M','2')
#define FOURCC_alac     GST_MAKE_FOURCC('a','l','a','c')
#define FOURCC_samr     GST_MAKE_FOURCC('s','a','m','r')
#define FOURCC_sawb     GST_MAKE_FOURCC('s','a','w','b')
#define FOURCC_mdat     GST_MAKE_FOURCC('m','d','a','t')
#define FOURCC_wide     GST_MAKE_FOURCC('w','i','d','e')
#define FOURCC_PICT     GST_MAKE_FOURCC('P','I','C','T')
#define FOURCC_pnot     GST_MAKE_FOURCC('p','n','o','t')
#define FOURCC_zlib     GST_MAKE_FOURCC('z','l','i','b')
#define FOURCC_alis     GST_MAKE_FOURCC('a','l','i','s')
#define FOURCC_url_     GST_MAKE_FOURCC('u','r','l',' ')
#define FOURCC_frma     GST_MAKE_FOURCC('f','r','m','a')
#define FOURCC_ctts     GST_MAKE_FOURCC('c','t','t','s')
#define FOURCC_drac     GST_MAKE_FOURCC('d','r','a','c')
#define FOURCC_jpeg     GST_MAKE_FOURCC('j','p','e','g')
#define FOURCC_mjp2     GST_MAKE_FOURCC('m','j','p','2')
#define FOURCC_jp2h     GST_MAKE_FOURCC('j','p','2','h')
#define FOURCC_jp2c     GST_MAKE_FOURCC('j','p','2','c')
#define FOURCC_gama     GST_MAKE_FOURCC('g','a','m','a')
#define FOURCC_tvsh     GST_MAKE_FOURCC('t','v','s','h')
#define FOURCC_tven     GST_MAKE_FOURCC('t','v','e','n')
#define FOURCC_tvsn     GST_MAKE_FOURCC('t','v','s','n')
#define FOURCC_tves     GST_MAKE_FOURCC('t','v','e','s')
#define FOURCC_sonm     GST_MAKE_FOURCC('s','o','n','m')
#define FOURCC_soal     GST_MAKE_FOURCC('s','o','a','l')
#define FOURCC_soar     GST_MAKE_FOURCC('s','o','a','r')
#define FOURCC_soaa     GST_MAKE_FOURCC('s','o','a','a')
#define FOURCC_soco     GST_MAKE_FOURCC('s','o','c','o')
#define FOURCC_sosn     GST_MAKE_FOURCC('s','o','s','n')
#define FOURCC_XMP_     GST_MAKE_FOURCC('X','M','P','_')
#define FOURCC_uuid     GST_MAKE_FOURCC('u','u','i','d')


/* SVQ3 fourcc */
#define FOURCC_SEQH     GST_MAKE_FOURCC('S','E','Q','H')
#define FOURCC_SMI_     GST_MAKE_FOURCC('S','M','I',' ')

/* fragmented mp4 */
#define FOURCC_mvex     GST_MAKE_FOURCC('m','v','e','x')
#define FOURCC_mehd     GST_MAKE_FOURCC('m','e','h','d')
#define FOURCC_trex     GST_MAKE_FOURCC('t','r','e','x')
#define FOURCC_mfra     GST_MAKE_FOURCC('m','f','r','a')
#define FOURCC_moof     GST_MAKE_FOURCC('m','o','o','f')
#define FOURCC_tfra     GST_MAKE_FOURCC('t','f','r','a')
#define FOURCC_tfhd     GST_MAKE_FOURCC('t','f','h','d')
#define FOURCC_trun     GST_MAKE_FOURCC('t','r','u','n')
#define FOURCC_sdtp     GST_MAKE_FOURCC('s','d','t','p')
#define FOURCC_mfro     GST_MAKE_FOURCC('m','f','r','o')
#define FOURCC_mfhd     GST_MAKE_FOURCC('m','f','h','d')
#define FOURCC_mvhd     GST_MAKE_FOURCC('m','v','h','d')
#define FOURCC_traf     GST_MAKE_FOURCC('t','r','a','f')

/* Xiph fourcc */
#define FOURCC_XiTh     GST_MAKE_FOURCC('X','i','T','h')
#define FOURCC_XdxT     GST_MAKE_FOURCC('X','d','x','T')
#define FOURCC_tCtH     GST_MAKE_FOURCC('t','C','t','H')
#define FOURCC_tCt_     GST_MAKE_FOURCC('t','C','t','#')
#define FOURCC_tCtC     GST_MAKE_FOURCC('t','C','t','C')

/* ilst metatags */
#define FOURCC_titl     GST_MAKE_FOURCC('t','i','t','l')
#define FOURCC__cmt     GST_MAKE_FOURCC(0xa9, 'c','m','t')

/* 3gp tags */
#define FOURCC_dscp     GST_MAKE_FOURCC('d','s','c','p')
#define FOURCC_perf     GST_MAKE_FOURCC('p','e','r','f')
#define FOURCC_auth     GST_MAKE_FOURCC('a','u','t','h')
#define FOURCC_yrrc     GST_MAKE_FOURCC('y','r','r','c')
#define FOURCC_albm     GST_MAKE_FOURCC('a','l','b','m')
#define FOURCC_loci     GST_MAKE_FOURCC('l','o','c','i')
#define FOURCC_kywd     GST_MAKE_FOURCC('k','y','w','d')
#define FOURCC_clsf     GST_MAKE_FOURCC('c','l','s','f')

/* For Microsoft Wave formats embedded in quicktime, the FOURCC is
   'm', 's', then the 16 bit wave codec id */
#define MS_WAVE_FOURCC(codecid)  GST_MAKE_FOURCC( \
        'm', 's', ((codecid)>>8)&0xff, ((codecid)&0xff))

#define FOURCC_owma     GST_MAKE_FOURCC('o','w','m','a')
#define FOURCC_ovc1     GST_MAKE_FOURCC('o','v','c','1')

G_END_DECLS

#endif /* __FOURCC_H__ */

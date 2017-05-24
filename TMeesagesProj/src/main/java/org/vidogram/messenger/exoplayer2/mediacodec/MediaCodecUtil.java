package org.vidogram.messenger.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.vidogram.messenger.exoplayer2.util.Util;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
public final class MediaCodecUtil
{
  private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST;
  private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST;
  private static final String CODEC_ID_AVC1 = "avc1";
  private static final String CODEC_ID_AVC2 = "avc2";
  private static final String CODEC_ID_HEV1 = "hev1";
  private static final String CODEC_ID_HVC1 = "hvc1";
  private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL;
  private static final MediaCodecInfo PASSTHROUGH_DECODER_INFO = MediaCodecInfo.newPassthroughInstance("OMX.google.raw.decoder");
  private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
  private static final String TAG = "MediaCodecUtil";
  private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = new HashMap();
  private static int maxH264DecodableFrameSize = -1;

  static
  {
    AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();
    AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
    AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
    AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
    AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
    AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
    AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
    AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
    AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
    AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
    AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
    AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
    AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
    AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
    AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
    AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
    AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
    AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
    AVC_LEVEL_NUMBER_TO_CONST.put(42, 8192);
    AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
    AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
    AVC_LEVEL_NUMBER_TO_CONST.put(52, 65536);
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", Integer.valueOf(1));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", Integer.valueOf(4));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", Integer.valueOf(16));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", Integer.valueOf(64));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", Integer.valueOf(256));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", Integer.valueOf(1024));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", Integer.valueOf(4096));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", Integer.valueOf(16384));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", Integer.valueOf(65536));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", Integer.valueOf(262144));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", Integer.valueOf(1048576));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", Integer.valueOf(4194304));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", Integer.valueOf(16777216));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", Integer.valueOf(2));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", Integer.valueOf(8));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", Integer.valueOf(32));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", Integer.valueOf(128));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", Integer.valueOf(512));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", Integer.valueOf(2048));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", Integer.valueOf(8192));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", Integer.valueOf(32768));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", Integer.valueOf(131072));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", Integer.valueOf(524288));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", Integer.valueOf(2097152));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", Integer.valueOf(8388608));
    HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", Integer.valueOf(33554432));
  }

  private static int avcLevelToMaxFrameSize(int paramInt)
  {
    int i = 25344;
    switch (paramInt)
    {
    default:
      i = -1;
    case 1:
    case 2:
      return i;
    case 8:
      return 101376;
    case 16:
      return 101376;
    case 32:
      return 101376;
    case 64:
      return 202752;
    case 128:
      return 414720;
    case 256:
      return 414720;
    case 512:
      return 921600;
    case 1024:
      return 1310720;
    case 2048:
      return 2097152;
    case 4096:
      return 2097152;
    case 8192:
      return 2228224;
    case 16384:
      return 5652480;
    case 32768:
    }
    return 9437184;
  }

  private static Pair<Integer, Integer> getAvcProfileAndLevel(String paramString, String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 2)
    {
      Log.w("MediaCodecUtil", "Ignoring malformed AVC codec string: " + paramString);
      return null;
    }
    Integer localInteger;
    try
    {
      if (paramArrayOfString[1].length() == 6)
      {
        localInteger = Integer.valueOf(Integer.parseInt(paramArrayOfString[1].substring(0, 2), 16));
        int i = Integer.parseInt(paramArrayOfString[1].substring(4), 16);
        paramString = Integer.valueOf(i);
        paramArrayOfString = localInteger;
      }
      while (true)
      {
        localInteger = Integer.valueOf(AVC_PROFILE_NUMBER_TO_CONST.get(paramArrayOfString.intValue()));
        if (localInteger != null)
          break;
        Log.w("MediaCodecUtil", "Unknown AVC profile: " + paramArrayOfString);
        return null;
        if (paramArrayOfString.length >= 3)
        {
          localInteger = Integer.valueOf(Integer.parseInt(paramArrayOfString[1]));
          paramArrayOfString = Integer.valueOf(Integer.parseInt(paramArrayOfString[2]));
          paramString = paramArrayOfString;
          paramArrayOfString = localInteger;
          continue;
        }
        Log.w("MediaCodecUtil", "Ignoring malformed AVC codec string: " + paramString);
        return null;
      }
    }
    catch (java.lang.NumberFormatException paramArrayOfString)
    {
      Log.w("MediaCodecUtil", "Ignoring malformed AVC codec string: " + paramString);
      return null;
    }
    paramArrayOfString = Integer.valueOf(AVC_LEVEL_NUMBER_TO_CONST.get(paramString.intValue()));
    if (paramArrayOfString == null)
    {
      Log.w("MediaCodecUtil", "Unknown AVC level: " + paramString);
      return null;
    }
    return new Pair(localInteger, paramArrayOfString);
  }

  public static Pair<Integer, Integer> getCodecProfileAndLevel(String paramString)
  {
    int i = 0;
    if (paramString == null)
      return null;
    String[] arrayOfString = paramString.split("\\.");
    String str = arrayOfString[0];
    switch (str.hashCode())
    {
    default:
      label64: i = -1;
    case 3199032:
    case 3214780:
    case 3006243:
    case 3006244:
    }
    while (true)
      switch (i)
      {
      default:
        return null;
      case 0:
      case 1:
        return getHevcProfileAndLevel(paramString, arrayOfString);
        if (!str.equals("hev1"))
          break label64;
        continue;
        if (!str.equals("hvc1"))
          break label64;
        i = 1;
        continue;
        if (!str.equals("avc1"))
          break label64;
        i = 2;
        continue;
        if (!str.equals("avc2"))
          break label64;
        i = 3;
      case 2:
      case 3:
      }
    return getAvcProfileAndLevel(paramString, arrayOfString);
  }

  public static MediaCodecInfo getDecoderInfo(String paramString, boolean paramBoolean)
  {
    paramString = getDecoderInfos(paramString, paramBoolean);
    if (paramString.isEmpty())
      return null;
    return (MediaCodecInfo)paramString.get(0);
  }

  public static List<MediaCodecInfo> getDecoderInfos(String paramString, boolean paramBoolean)
  {
    monitorenter;
    label208: 
    while (true)
    {
      try
      {
        CodecKey localCodecKey = new CodecKey(paramString, paramBoolean);
        localObject = (List)decoderInfosCache.get(localCodecKey);
        if (localObject == null)
          continue;
        paramString = (String)localObject;
        return paramString;
        if (Util.SDK_INT >= 21)
        {
          localObject = new MediaCodecListCompatV21(paramBoolean);
          List localList = getDecoderInfosInternal(localCodecKey, (MediaCodecListCompat)localObject);
          localObject = localList;
          if (!paramBoolean)
            continue;
          localObject = localList;
          if (!localList.isEmpty())
            continue;
          localObject = localList;
          if (21 > Util.SDK_INT)
            continue;
          localObject = localList;
          if (Util.SDK_INT > 23)
            continue;
          localObject = getDecoderInfosInternal(localCodecKey, new MediaCodecListCompatV16(null));
          if (((List)localObject).isEmpty())
            break label208;
          Log.w("MediaCodecUtil", "MediaCodecList API didn't list secure decoder for: " + paramString + ". Assuming: " + ((MediaCodecInfo)((List)localObject).get(0)).name);
          break label208;
          paramString = Collections.unmodifiableList((List)localObject);
          decoderInfosCache.put(localCodecKey, paramString);
          continue;
        }
      }
      finally
      {
        monitorexit;
      }
      Object localObject = new MediaCodecListCompatV16(null);
      continue;
    }
  }

  private static List<MediaCodecInfo> getDecoderInfosInternal(CodecKey paramCodecKey, MediaCodecListCompat paramMediaCodecListCompat)
  {
    while (true)
    {
      int i;
      String str2;
      int j;
      String str3;
      try
      {
        ArrayList localArrayList = new ArrayList();
        String str1 = paramCodecKey.mimeType;
        int k = paramMediaCodecListCompat.getCodecCount();
        boolean bool1 = paramMediaCodecListCompat.secureDecodersExplicit();
        i = 0;
        if (i >= k)
          continue;
        android.media.MediaCodecInfo localMediaCodecInfo = paramMediaCodecListCompat.getCodecInfoAt(i);
        str2 = localMediaCodecInfo.getName();
        if (!isCodecUsableDecoder(localMediaCodecInfo, str2, bool1))
          break label337;
        String[] arrayOfString = localMediaCodecInfo.getSupportedTypes();
        int m = arrayOfString.length;
        j = 0;
        if (j >= m)
          break label337;
        str3 = arrayOfString[j];
        boolean bool2 = str3.equalsIgnoreCase(str1);
        if (!bool2)
          break label344;
        try
        {
          MediaCodecInfo.CodecCapabilities localCodecCapabilities = localMediaCodecInfo.getCapabilitiesForType(str3);
          bool2 = paramMediaCodecListCompat.isSecurePlaybackSupported(str1, localCodecCapabilities);
          if (((!bool1) || (paramCodecKey.secure != bool2)) && ((bool1) || (paramCodecKey.secure)))
            continue;
          localArrayList.add(MediaCodecInfo.newInstance(str2, str1, localCodecCapabilities));
          break label344;
          if ((bool1) || (!bool2))
            break label344;
          localArrayList.add(MediaCodecInfo.newInstance(str2 + ".secure", str1, localCodecCapabilities));
          return localArrayList;
        }
        catch (Exception localException)
        {
          if (Util.SDK_INT > 23)
            break label290;
        }
        if (!localArrayList.isEmpty())
          Log.e("MediaCodecUtil", "Skipping codec " + str2 + " (failed to query capabilities)");
      }
      catch (Exception paramCodecKey)
      {
        throw new DecoderQueryException(paramCodecKey, null);
      }
      label290: Log.e("MediaCodecUtil", "Failed to query codec " + str2 + " (" + str3 + ")");
      throw localException;
      label337: i += 1;
      continue;
      label344: j += 1;
    }
  }

  private static Pair<Integer, Integer> getHevcProfileAndLevel(String paramString, String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 4)
    {
      Log.w("MediaCodecUtil", "Ignoring malformed HEVC codec string: " + paramString);
      return null;
    }
    Matcher localMatcher = PROFILE_PATTERN.matcher(paramArrayOfString[1]);
    if (!localMatcher.matches())
    {
      Log.w("MediaCodecUtil", "Ignoring malformed HEVC codec string: " + paramString);
      return null;
    }
    paramString = localMatcher.group(1);
    int i;
    if ("1".equals(paramString))
      i = 1;
    while (true)
    {
      paramString = (Integer)HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(paramArrayOfString[3]);
      if (paramString != null)
        break;
      Log.w("MediaCodecUtil", "Unknown HEVC level string: " + localMatcher.group(1));
      return null;
      if ("2".equals(paramString))
      {
        i = 2;
        continue;
      }
      Log.w("MediaCodecUtil", "Unknown HEVC profile string: " + paramString);
      return null;
    }
    return new Pair(Integer.valueOf(i), paramString);
  }

  public static MediaCodecInfo getPassthroughDecoderInfo()
  {
    return PASSTHROUGH_DECODER_INFO;
  }

  private static boolean isCodecUsableDecoder(android.media.MediaCodecInfo paramMediaCodecInfo, String paramString, boolean paramBoolean)
  {
    if ((paramMediaCodecInfo.isEncoder()) || ((!paramBoolean) && (paramString.endsWith(".secure"))));
    do
      return false;
    while (((Util.SDK_INT < 21) && (("CIPAACDecoder".equals(paramString)) || ("CIPMP3Decoder".equals(paramString)) || ("CIPVorbisDecoder".equals(paramString)) || ("CIPAMRNBDecoder".equals(paramString)) || ("AACDecoder".equals(paramString)) || ("MP3Decoder".equals(paramString)))) || ((Util.SDK_INT < 18) && ("OMX.SEC.MP3.Decoder".equals(paramString))) || ((Util.SDK_INT < 18) && ("OMX.MTK.AUDIO.DECODER.AAC".equals(paramString)) && ("a70".equals(Util.DEVICE))) || ((Util.SDK_INT == 16) && ("OMX.qcom.audio.decoder.mp3".equals(paramString)) && (("dlxu".equals(Util.DEVICE)) || ("protou".equals(Util.DEVICE)) || ("ville".equals(Util.DEVICE)) || ("villeplus".equals(Util.DEVICE)) || ("villec2".equals(Util.DEVICE)) || (Util.DEVICE.startsWith("gee")) || ("C6602".equals(Util.DEVICE)) || ("C6603".equals(Util.DEVICE)) || ("C6606".equals(Util.DEVICE)) || ("C6616".equals(Util.DEVICE)) || ("L36h".equals(Util.DEVICE)) || ("SO-02E".equals(Util.DEVICE)))) || ((Util.SDK_INT == 16) && ("OMX.qcom.audio.decoder.aac".equals(paramString)) && (("C1504".equals(Util.DEVICE)) || ("C1505".equals(Util.DEVICE)) || ("C1604".equals(Util.DEVICE)) || ("C1605".equals(Util.DEVICE)))) || ((Util.SDK_INT <= 19) && ((Util.DEVICE.startsWith("d2")) || (Util.DEVICE.startsWith("serrano")) || (Util.DEVICE.startsWith("jflte")) || (Util.DEVICE.startsWith("santos"))) && ("samsung".equals(Util.MANUFACTURER)) && ("OMX.SEC.vp8.dec".equals(paramString))) || ((Util.SDK_INT <= 19) && (Util.DEVICE.startsWith("jflte")) && ("OMX.qcom.video.decoder.vp8".equals(paramString))));
    return true;
  }

  public static int maxH264DecodableFrameSize()
  {
    int i = 0;
    int j = 0;
    if (maxH264DecodableFrameSize == -1)
    {
      Object localObject = getDecoderInfo("video/avc", false);
      if (localObject != null)
      {
        localObject = ((MediaCodecInfo)localObject).getProfileLevels();
        int k = localObject.length;
        i = 0;
        while (j < k)
        {
          i = Math.max(avcLevelToMaxFrameSize(localObject[j].level), i);
          j += 1;
        }
        if (Util.SDK_INT < 21)
          break label85;
      }
    }
    label85: for (j = 345600; ; j = 172800)
    {
      i = Math.max(i, j);
      maxH264DecodableFrameSize = i;
      return maxH264DecodableFrameSize;
    }
  }

  public static void warmDecoderInfoCache(String paramString, boolean paramBoolean)
  {
    try
    {
      getDecoderInfos(paramString, paramBoolean);
      return;
    }
    catch (DecoderQueryException paramString)
    {
      Log.e("MediaCodecUtil", "Codec warming failed", paramString);
    }
  }

  private static final class CodecKey
  {
    public final String mimeType;
    public final boolean secure;

    public CodecKey(String paramString, boolean paramBoolean)
    {
      this.mimeType = paramString;
      this.secure = paramBoolean;
    }

    public boolean equals(Object paramObject)
    {
      if (this == paramObject);
      do
      {
        return true;
        if ((paramObject == null) || (paramObject.getClass() != CodecKey.class))
          return false;
        paramObject = (CodecKey)paramObject;
      }
      while ((TextUtils.equals(this.mimeType, paramObject.mimeType)) && (this.secure == paramObject.secure));
      return false;
    }

    public int hashCode()
    {
      int i;
      if (this.mimeType == null)
      {
        i = 0;
        if (!this.secure)
          break label41;
      }
      label41: for (int j = 1231; ; j = 1237)
      {
        return j + (i + 31) * 31;
        i = this.mimeType.hashCode();
        break;
      }
    }
  }

  public static class DecoderQueryException extends Exception
  {
    private DecoderQueryException(Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }

  private static abstract interface MediaCodecListCompat
  {
    public abstract int getCodecCount();

    public abstract android.media.MediaCodecInfo getCodecInfoAt(int paramInt);

    public abstract boolean isSecurePlaybackSupported(String paramString, MediaCodecInfo.CodecCapabilities paramCodecCapabilities);

    public abstract boolean secureDecodersExplicit();
  }

  private static final class MediaCodecListCompatV16
    implements MediaCodecUtil.MediaCodecListCompat
  {
    public int getCodecCount()
    {
      return MediaCodecList.getCodecCount();
    }

    public android.media.MediaCodecInfo getCodecInfoAt(int paramInt)
    {
      return MediaCodecList.getCodecInfoAt(paramInt);
    }

    public boolean isSecurePlaybackSupported(String paramString, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      return "video/avc".equals(paramString);
    }

    public boolean secureDecodersExplicit()
    {
      return false;
    }
  }

  @TargetApi(21)
  private static final class MediaCodecListCompatV21
    implements MediaCodecUtil.MediaCodecListCompat
  {
    private final int codecKind;
    private android.media.MediaCodecInfo[] mediaCodecInfos;

    public MediaCodecListCompatV21(boolean paramBoolean)
    {
      if (paramBoolean);
      for (int i = 1; ; i = 0)
      {
        this.codecKind = i;
        return;
      }
    }

    private void ensureMediaCodecInfosInitialized()
    {
      if (this.mediaCodecInfos == null)
        this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
    }

    public int getCodecCount()
    {
      ensureMediaCodecInfosInitialized();
      return this.mediaCodecInfos.length;
    }

    public android.media.MediaCodecInfo getCodecInfoAt(int paramInt)
    {
      ensureMediaCodecInfosInitialized();
      return this.mediaCodecInfos[paramInt];
    }

    public boolean isSecurePlaybackSupported(String paramString, MediaCodecInfo.CodecCapabilities paramCodecCapabilities)
    {
      return paramCodecCapabilities.isFeatureSupported("secure-playback");
    }

    public boolean secureDecodersExplicit()
    {
      return true;
    }
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.exoplayer2.mediacodec.MediaCodecUtil
 * JD-Core Version:    0.6.0
 */
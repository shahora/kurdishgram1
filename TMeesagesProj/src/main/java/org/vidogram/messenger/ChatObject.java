package org.vidogram.messenger;

import org.vidogram.tgnet.TLRPC.Chat;
import org.vidogram.tgnet.TLRPC.TL_channel;
import org.vidogram.tgnet.TLRPC.TL_channelForbidden;
import org.vidogram.tgnet.TLRPC.TL_chatEmpty;
import org.vidogram.tgnet.TLRPC.TL_chatForbidden;

public class ChatObject
{
  public static final int CHAT_TYPE_BROADCAST = 1;
  public static final int CHAT_TYPE_CHANNEL = 2;
  public static final int CHAT_TYPE_CHAT = 0;
  public static final int CHAT_TYPE_MEGAGROUP = 4;
  public static final int CHAT_TYPE_USER = 3;

  public static boolean canWriteToChat(TLRPC.Chat paramChat)
  {
    return (!isChannel(paramChat)) || (paramChat.creator) || (paramChat.editor) || (!paramChat.broadcast);
  }

  public static TLRPC.Chat getChatByDialog(long paramLong)
  {
    int i = (int)paramLong;
    int j = (int)(paramLong >> 32);
    if (i < 0)
      return MessagesController.getInstance().getChat(Integer.valueOf(-i));
    return null;
  }

  public static boolean isCanWriteToChannel(int paramInt)
  {
    TLRPC.Chat localChat = MessagesController.getInstance().getChat(Integer.valueOf(paramInt));
    return (localChat != null) && ((localChat.creator) || (localChat.editor) || (localChat.megagroup));
  }

  public static boolean isChannel(int paramInt)
  {
    TLRPC.Chat localChat = MessagesController.getInstance().getChat(Integer.valueOf(paramInt));
    return ((localChat instanceof TLRPC.TL_channel)) || ((localChat instanceof TLRPC.TL_channelForbidden));
  }

  public static boolean isChannel(TLRPC.Chat paramChat)
  {
    return ((paramChat instanceof TLRPC.TL_channel)) || ((paramChat instanceof TLRPC.TL_channelForbidden));
  }

  public static boolean isKickedFromChat(TLRPC.Chat paramChat)
  {
    return (paramChat == null) || ((paramChat instanceof TLRPC.TL_chatEmpty)) || ((paramChat instanceof TLRPC.TL_chatForbidden)) || ((paramChat instanceof TLRPC.TL_channelForbidden)) || (paramChat.kicked) || (paramChat.deactivated);
  }

  public static boolean isLeftFromChat(TLRPC.Chat paramChat)
  {
    return (paramChat == null) || ((paramChat instanceof TLRPC.TL_chatEmpty)) || ((paramChat instanceof TLRPC.TL_chatForbidden)) || ((paramChat instanceof TLRPC.TL_channelForbidden)) || (paramChat.left) || (paramChat.deactivated);
  }

  public static boolean isNotInChat(TLRPC.Chat paramChat)
  {
    return (paramChat == null) || ((paramChat instanceof TLRPC.TL_chatEmpty)) || ((paramChat instanceof TLRPC.TL_chatForbidden)) || ((paramChat instanceof TLRPC.TL_channelForbidden)) || (paramChat.left) || (paramChat.kicked) || (paramChat.deactivated);
  }
}

/* Location:           C:\Documents and Settings\soran\Desktop\s\classes.jar
 * Qualified Name:     org.vidogram.messenger.ChatObject
 * JD-Core Version:    0.6.0
 */
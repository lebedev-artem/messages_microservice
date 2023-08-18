package dto;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter

public class DialogDto {

    @NonNull
    String id;


    boolean isDeleted;

    //Количество непрочитанных сообщений диалога
    Integer unreadCount;

    //UUID первого собеседника
    @NonNull
    String conversationPartner1;

    //UUID второго собеседника
    @NonNull
    String conversationPartner2;

    MessageDto lastMessage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getConversationPartner1() {
        return conversationPartner1;
    }

    public void setConversationPartner1(String conversationPartner1) {
        this.conversationPartner1 = conversationPartner1;
    }

    public String getConversationPartner2() {
        return conversationPartner2;
    }

    public void setConversationPartner2(String conversationPartner2) {
        this.conversationPartner2 = conversationPartner2;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage() {
        this.lastMessage = lastMessage;
    }
}

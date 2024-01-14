package login.security.event;

import login.security.Entity.Member;
import org.springframework.context.ApplicationEvent;

public class MemberJoinEvent extends ApplicationEvent {

    private Member member;
    public MemberJoinEvent(Member member) {
        super(member);
        this.member=member;
    }

    public Member getMember(){
        return member;
    }
}

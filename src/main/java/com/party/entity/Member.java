package com.party.entity;

import com.party.constant.Role;
import com.party.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter@Setter@ToString
@EntityListeners(value = {AuditingEntityListener.class})
public class Member extends BaseEntity  {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    private String address;

    private String phone;

    private String category;

    private LocalDateTime regdate ;

    @Enumerated(EnumType.STRING)
    private Role role;

    //화면에서 넘어오는 dto와  비번 암호화 객체를 이용하여 Member 엔터티 객체 생성하는 메소드
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPhone(memberFormDto.getPhone());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        member.setCategory(memberFormDto.getCategory());

        LocalDateTime now = LocalDateTime.now(); // 현재 시간을 생성
        member.setRegdate(now); // 현재 시간을 자동으로 저장

        return member;
    }


    public LocalDateTime getRegdate() {
        return regdate;
    }


    public void setUsername(String loggedInUsername) {
    }
}


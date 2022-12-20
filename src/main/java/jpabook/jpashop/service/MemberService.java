package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기전용으로 실행
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
/*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

*/

    @Transactional //데이터가 변경되는 구간이므로 선언
    public long join(Member member) {
        validateDuplicateMember(member); //중복회원 검사
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 한명 조회
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }


    //@Transactional : 선언적 트랜잭션 처리를 지원한다.
    //변경 감지 시작
    // .. 트랜젝션 시작 .. 영속성 Entity 조회 (없으면 DB 조회 후 영속화) .. 조회한 영속성 Entity 데이터 수정 .. 트랜잭션 커밋
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}

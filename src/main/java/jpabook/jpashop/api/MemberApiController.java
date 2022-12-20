package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //Post : 등록, Put : 수정

    //파라미터에 entity 그대로 받지말고 dto를 만들어서 하는게 좋다. v2 참고
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //@ReuqestBody : JSON 타입으로 요청이 왔을때 JSON을 JAVA 객체로 변환
    //객체를 따로 만들고 Valid처리 후 NotEmpty를 따로 해준다. Entity는 건드리지 않는다
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV1(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        //service에서 변경감지를 꼭 사용하자
        memberService.update(id, request.getName());

    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }


    @Data
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    //@RequiredArgsConstructor 초기화 되지 않은 final 필드와 @NonNull 어노테이션이 붙은 필드에 대한 생성자 생성
    //@AllArgsConstructor 모든 필드에 대한 생성자 생성.
    @Data
    @AllArgsConstructor
    static class UpdateMemberRequest {
        private Long id;
        private String name;
    }

}

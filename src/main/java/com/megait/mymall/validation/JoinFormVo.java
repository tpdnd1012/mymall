package com.megait.mymall.validation;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * 회원가입 뷰에서 form의 파라미터를 받을 포장 객체
 *  ** VO(Value Object) ==> 읽기전용 dto
 */

@Data
public class JoinFormVo {

    @NotNull // null이면 안된다.
    @NotBlank // empty string 이면 안된다. ("")
    @Length(min=5, max=40, message="이메일은 5자 이상 40자 이하여야합니다.")
    @Email(message = "이메일 형식을 지켜주세요. (예. you@test.com")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,}$",
            message = "패스워드는 영문자, 숫자, 특수기호를 조합하여 최소 8자 이상을 입력하셔야 합니다."
    )
    private String password;

    @AssertTrue(message = "반드시 약관에 동의하셔야합니다.")
    private boolean agreeTermsOfService;

    @Pattern(regexp = "(^[0-9]+$|^$)")
    private String postcode;

    private String baseAddress;

    private String detailAddress;

    // 주소 칸 3개가 모두 비어있거나, 모두 채워져야 함.
    @AssertTrue(message = "올바른 주소 형식을 지켜주세요.")
    public boolean isValidAddress(){
        return
                (postcode == null && baseAddress == null && detailAddress == null)
                || (postcode.isBlank() && baseAddress.isBlank() && detailAddress.isBlank())
                || (!postcode.isBlank() && !baseAddress.isBlank() && !detailAddress.isBlank());
    }
}



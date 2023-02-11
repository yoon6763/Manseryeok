package com.example.manseryeok.utils

import com.example.manseryeok.models.TimeZone

object TimeDiffConstants {
    val timeZones = arrayOf(
        TimeZone("대한민국", -30),
        TimeZone("일본", 0),
        TimeZone("중국", -60),
        TimeZone("홍콩", -60),
        TimeZone("대만", -60),
        TimeZone("필리핀", -60),
        TimeZone("덴파샤(인도네시아)", -60),
        TimeZone("싱가폴", -60),
        TimeZone("말레이시아", -60),
        TimeZone("몽고", -60),
        TimeZone("마카오", -60),
        TimeZone("태국", -120),
        TimeZone("자카르타(인도네시아)", -120),
        TimeZone("베트남", -120),
        TimeZone("캄보디아", -120),
        TimeZone("미얀마", -150),
        TimeZone("방글라데시", -180),
        TimeZone("네팔", 195),
        TimeZone("인도", -210),
        TimeZone("스리랑카", -210),
        TimeZone("파키스탄", -240),
        TimeZone("아랍에미레이트", -300),
        TimeZone("이란", -330),
        TimeZone("사우디아라비아", -360),
        TimeZone("쿠웨이트", -360),
        TimeZone("시리아", -420),
        TimeZone("요르단", -420),
        TimeZone("이집트", -420),
        TimeZone("이스라엘", -420),
        TimeZone("케냐", -360),
        TimeZone("남아프리카공화국", -420),
        TimeZone("나이지리아", -480),
        TimeZone("리비아", -480),
        TimeZone("가나", -540),
        TimeZone("모로코", -540),
        TimeZone("카나리섬", -540),
        TimeZone("터키", -420),
        TimeZone("그리스", -420),
        TimeZone("핀란드", -420),
        TimeZone("루마니아", -420),
        TimeZone("불가리아", -420),
        TimeZone("독일", -480),
        TimeZone("프랑스", -480),
        TimeZone("네덜란드", -480),
        TimeZone("이탈리아", -480),
        TimeZone("스웨덴", -480),
        TimeZone("스위스", -480),
        TimeZone("스페인", -480),
        TimeZone("오스트리아", -480),
        TimeZone("벨기에", -480),
        TimeZone("폴란드", -480),
        TimeZone("덴마크", -480),
        TimeZone("노르웨이", -480),
        TimeZone("헝가리", -480),
        TimeZone("체코", -480),
        TimeZone("영국", -540),
        TimeZone("아일랜드", -540),
        TimeZone("포르투갈", -540),
        TimeZone("브라질리아(브라질)", -720),
        TimeZone("상파울로(브라질)", -720),
        TimeZone("살바도로(브라질)", -720),
        TimeZone("아르헨티나", -720),
        TimeZone("칠레", -780),
        TimeZone("파라과이", -780),
        TimeZone("파나마", -840),
        TimeZone("페루", -840),
        TimeZone("콜롬비아", -840),
        TimeZone("멕시코", -900),
        TimeZone("과테말라", -900),
        TimeZone("온두라스", -900),
        TimeZone("뉴욕(미국)", -840),
        TimeZone("워싱턴(미국)", -840),
        TimeZone("토론토(캐나다)", -840),
        TimeZone("오타와(캐나다)", -840),
        TimeZone("휴스턴(미국)", -900),
        TimeZone("시카고(미국)", -900),
        TimeZone("피닉스(미국)", -960),
        TimeZone("덴버(미국)", -960),
        TimeZone("캘거리(미국)", -960),
        TimeZone("밴쿠버(캐나다)", -1020),
        TimeZone("시애틀(미국)", -1020),
        TimeZone("샌프란시스코(미국)", -1020),
        TimeZone("LA(미국)", -1020),
        TimeZone("산타모니카(미국)", -1020),
        TimeZone("앵커리지(미국)", 1080),
        TimeZone("하와이(미국)", 1140),
        TimeZone("프리맨틀(호주)", -60),
        TimeZone("애들레이드(호주)", 30),
        TimeZone("시드니(호주)", 60),
        TimeZone("캔버라(호주)", 60),
        TimeZone("괌", 60),
        TimeZone("사이판", 60),
        TimeZone("파푸아뉴기니야", 60),
        TimeZone("뉴질랜드", 180),
        TimeZone("피지", 180),
        TimeZone("페트로파블로브스크(러시아)", 180),
        TimeZone("사할린(러시아)", 120),
        TimeZone("블라드보스톡", 60),
        TimeZone("우즈베키스탄", -180),
        TimeZone("카자흐스탄", -180),
        TimeZone("모스크바", -360),
        TimeZone("레닌그라드", -360),
        TimeZone("몰도바", -420),
        TimeZone("우크라이나", -420)
    )
}

// -1 중국
// -1 홍콩
// -1 대만
// -1 필리핀
// -1 덴파샤(인도네시아)
// -1 싱가폴
// -1 말레이시아
// -1 몽고
// -1 마카오
// -2 태국
// -2 자카르타(인도네시아)
// -2 베트남
// -2 캄보디아
// -2 30 미얀마
// -3 방글라데시
// -3 15 네팔
// -3 30 인도
// -3 30 스리랑카
// -4 파키스탄
// -5 아랍에미레이트
// -5 30 이란
// -6 사우디아라비아
// -6 쿠웨이트
// -7 시리아
// -7 요르단
// -7 이집트
// -7 이스라엘
// -6 케냐
// -7 남아프리카공화국
// -8 나이지리아
// -8 리비아
// -9 가나
// -9 모로코
// -9 카나리섬
// -7 터키
// -7 그리스
// -7 핀란드
// -7 루마니아
// -7 불가리아
// -8 독일
// -8 프랑스
// -8 네덜란드
// -8 이탈리아
// -8 스웨덴
// -8 스위스
// -8 스페인
// -8 오스트리아
// -8 벨기에
// -8 폴란드
// -8 덴마크
// -8 노르웨이
// -8 헝가리
// -8 체코
// -9 영국
// -9 아일랜드
// -9 포르투갈
// -12 브라질리아(브라질)
// -12 상파울로(브라질)
// -12 살바도로(브라질)
// -12 아르헨티나
// -13 칠레
// -13 파라과이
// -14 파나마
// -14 페루
// -14 콜롬비아
// -15 멕시코
// -15 과테말라
// -15 온두라스
// -14 뉴욕(미국)
// -14 워싱턴(미국)
// -14 토론토(캐나다)
// -14 오타와(캐나다)
// -15 휴스턴(미국)
// -15 시카고(미국)
// -16 피닉스(미국)
// -16 덴버(미국)
// -16 캘거리(미국)
// -17 밴쿠버(캐나다)
// -17 시애틀(미국)
// -17 샌프란시스코(미국)
// -17 LA(미국)
// -17 산타모니카(미국)
// -18 앵커리지(미국)
// -19 하와이(미국)
// -1 프리맨틀(호주)
// 30 애들레이드(호주)
// 1 시드니(호주)
// 1 캔버라(호주)
// 1 괌
// 1 사이판
// 1 파푸아뉴기니야
// 3 뉴질랜드
// 3 피지
// 3 페트로파블로브스크(러시아)
// 2 사할린(러시아)
// 1 블라드보스톡
// -3 우즈베키스탄
// -3 카자흐스탄
// -6 모스크바
// -6 레닌그라드
// -7 몰도바
// -7 우크라이나
// 페이지 로드 시 3초 후 /color로 이동
window.addEventListener('DOMContentLoaded', function() {
    setTimeout(function() {
        // GET 요청으로 /color로 이동
        window.location.href = '/color';
    }, 3000); // 3초 = 3000ms
});

// 선택사항: 카운트다운 표시
let countdown = 3;
const countdownInterval = setInterval(function() {
    countdown--;
    if (countdown <= 0) {
        clearInterval(countdownInterval);
    }
}, 1000);
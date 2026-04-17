function switchTab(tab) {
    const loginSection = document.getElementById('section-login');
    const signupSection = document.getElementById('section-signup');
    const loginBtn = document.getElementById('tab-login');
    const signupBtn = document.getElementById('tab-signup');

    if (tab === 'login') {
        loginSection.classList.remove('hidden');
        signupSection.classList.add('hidden');
        loginBtn.classList.add('active');
        signupBtn.classList.remove('active');
    } else {
        loginSection.classList.add('hidden');
        signupSection.classList.remove('hidden');
        signupBtn.classList.add('active');
        loginBtn.classList.remove('active');
    }
}

function handleLogin() {
    const loginId = document.getElementById('login-id').value.trim();
    const loginPw = document.getElementById('login-pw').value.trim();

    if (!loginId || !loginPw) {
        alert('아이디와 비밀번호를 입력해주세요.');
        return;
    }

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `loginId=${encodeURIComponent(loginId)}&password=${encodeURIComponent(loginPw)}`
    })
    .then(res => {
        if (!res.ok) return res.json().then(e => { throw new Error(e.error); });
        return res.json();
    })
    .then(data => {
        localStorage.setItem('token', data.token);
        window.location.href = '/choose';
    })
    .catch(err => {
        alert(err.message || '로그인에 실패했습니다.');
    });
}

function handleSignup() {
    // 백엔드 미구현 — 추후 연결
    alert('회원가입 기능은 준비 중입니다.');
}

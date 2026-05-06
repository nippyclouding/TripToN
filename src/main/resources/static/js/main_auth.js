/* ============================
 * Sign Modal
 * ============================ */

function openSignModal() {
    closeSidebar();
    document.getElementById('sign-modal').classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeSignModal() {
    document.getElementById('sign-modal').classList.remove('active');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') closeSignModal();
});

/* ============================
 * Auth Handlers
 * ============================ */

function handleSignOut() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/auth/signOut';
    document.body.appendChild(form);
    form.submit();
}

function handleMyPage() {
    window.location.href = '/myPage';
}

function switchAuthTab(tab) {
    const signinForm = document.getElementById('form-signin');
    const signupForm = document.getElementById('form-signup');
    const signinTab  = document.getElementById('tab-signin');
    const signupTab  = document.getElementById('tab-signup');

    if (tab === 'signin') {
        signinForm.classList.remove('hidden');
        signupForm.classList.add('hidden');
        signinTab.classList.add('active');
        signupTab.classList.remove('active');
    } else {
        signinForm.classList.add('hidden');
        signupForm.classList.remove('hidden');
        signupTab.classList.add('active');
        signinTab.classList.remove('active');
    }
}

function handleSignIn() {
    const email = document.querySelector('#form-signin [name="memberEmail"]').value.trim();
    const pw    = document.querySelector('#form-signin [name="memberLoginPassword"]').value.trim();

    if (!email || !pw) {
        alert('이메일과 비밀번호를 입력해주세요.');
        return;
    }

    document.getElementById('form-signin').submit();
}

function handleSignUp() {
    const email     = document.querySelector('#form-signup [name="memberEmail"]').value.trim();
    const nickname  = document.querySelector('#form-signup [name="memberNickName"]').value.trim();
    const pw        = document.getElementById('signup-pw').value.trim();
    const pwConfirm = document.getElementById('signup-pw-confirm').value.trim();

    if (!email || !nickname || !pw || !pwConfirm) {
        alert('모든 항목을 입력해주세요.');
        return;
    }

    if (pw !== pwConfirm) {
        alert('비밀번호가 일치하지 않습니다.');
        document.getElementById('signup-pw-confirm').focus();
        return;
    }

    document.getElementById('form-signup').submit();
}

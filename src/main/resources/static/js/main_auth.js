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

let signUpEmailCheck = {
    email: '',
    available: false
};

let signUpNicknameCheck = {
    nickname: '',
    available: false
};

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

    if (!signUpEmailCheck.available || signUpEmailCheck.email !== email) {
        setEmailCheckMessage('이메일 검증을 완료해주세요.', false);
        return;
    }

    if (!signUpNicknameCheck.available || signUpNicknameCheck.nickname !== nickname) {
        setNicknameCheckMessage('닉네임 검증을 완료해주세요.', false);
        return;
    }

    if (pw !== pwConfirm) {
        alert('비밀번호가 일치하지 않습니다.');
        document.getElementById('signup-pw-confirm').focus();
        return;
    }

    document.getElementById('form-signup').submit();
}

async function checkSignUpEmail() {
    const emailInput = document.querySelector('#form-signup [name="memberEmail"]');
    const email = emailInput.value.trim();

    signUpEmailCheck = { email: '', available: false };

    if (!email) {
        setEmailCheckMessage('이메일을 입력해주세요.', false);
        emailInput.focus();
        return;
    }

    if (!emailInput.checkValidity()) {
        setEmailCheckMessage('올바른 이메일 형식이 아닙니다.', false);
        emailInput.focus();
        return;
    }

    try {
        const res = await fetch('/auth/check-email-unique', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email })
        });

        if (!res.ok) {
            setEmailCheckMessage('이메일 검증에 실패했습니다.', false);
            return;
        }

        const isUnique = await res.json();
        signUpEmailCheck = { email, available: isUnique };
        setEmailCheckMessage(isUnique ? '사용 가능' : '다른 이메일을 사용해주세요', isUnique);
    } catch (error) {
        setEmailCheckMessage('이메일 검증에 실패했습니다.', false);
    }
}

async function checkSignUpNickname() {
    const nicknameInput = document.querySelector('#form-signup [name="memberNickName"]');
    const nickname = nicknameInput.value.trim();

    signUpNicknameCheck = { nickname: '', available: false };

    if (!nickname) {
        setNicknameCheckMessage('닉네임을 입력해주세요.', false);
        nicknameInput.focus();
        return;
    }

    try {
        const res = await fetch('/auth/check-nickname-unique', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nickname })
        });

        if (!res.ok) {
            setNicknameCheckMessage('닉네임 검증에 실패했습니다.', false);
            return;
        }

        const isUnique = await res.json();
        signUpNicknameCheck = { nickname, available: isUnique };
        setNicknameCheckMessage(isUnique ? '사용 가능' : '다른 닉네임을 사용해주세요', isUnique);
    } catch (error) {
        setNicknameCheckMessage('닉네임 검증에 실패했습니다.', false);
    }
}

function setEmailCheckMessage(message, isAvailable) {
    const messageEl = document.getElementById('signup-email-check-message');
    if (!messageEl) return;

    messageEl.textContent = message;
    messageEl.classList.toggle('sign-message--success', isAvailable);
    messageEl.classList.toggle('sign-message--fail', !isAvailable);
}

function setNicknameCheckMessage(message, isAvailable) {
    const messageEl = document.getElementById('signup-nickname-check-message');
    if (!messageEl) return;

    messageEl.textContent = message;
    messageEl.classList.toggle('sign-message--success', isAvailable);
    messageEl.classList.toggle('sign-message--fail', !isAvailable);
}

document.addEventListener('DOMContentLoaded', () => {
    const signModal = document.getElementById('sign-modal');
    if (signModal && signModal.dataset.authOpen === 'true') {
        openSignModal();
        switchAuthTab(signModal.dataset.authTab === 'signup' ? 'signup' : 'signin');
    }

    const emailInput = document.querySelector('#form-signup [name="memberEmail"]');
    const nicknameInput = document.querySelector('#form-signup [name="memberNickName"]');

    if (emailInput) {
        emailInput.addEventListener('input', () => {
            signUpEmailCheck = { email: '', available: false };
            setEmailCheckMessage('', false);
        });
    }

    if (nicknameInput) {
        nicknameInput.addEventListener('input', () => {
            signUpNicknameCheck = { nickname: '', available: false };
            setNicknameCheckMessage('', false);
        });
    }
});

function postForm(url, data = {}) {
    return fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams(data)
    });
}

function deleteRequest(url) {
    return fetch(url, { method: 'DELETE' });
}

function patchForm(url, data = {}) {
    return fetch(url, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams(data)
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const detail = window.detailPage || {};
    const concernId = detail.concernId;

    const likeBtn = document.getElementById('like-btn');
    const likeIcon = document.getElementById('like-icon');
    const likeCount = document.getElementById('like-count');

    if (likeBtn && !likeBtn.disabled) {
        likeBtn.addEventListener('click', async () => {
            const res = await fetch(`/concern/api/${concernId}/like`, { method: 'POST' });
            if (!res.ok) {
                alert('좋아요 처리에 실패했습니다.');
                return;
            }

            const liked = likeBtn.classList.toggle('liked');
            likeIcon.textContent = liked ? '♥' : '♡';
            likeCount.textContent = liked
                ? Number(likeCount.textContent) + 1
                : Number(likeCount.textContent) - 1;
        });
    }

    const editModal = document.getElementById('concern-edit-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalContent = document.getElementById('modal-content');

    const concernEditBtn = document.getElementById('concern-edit-btn');
    const modalCancelBtn = document.getElementById('modal-cancel-btn');
    const modalConfirmBtn = document.getElementById('modal-confirm-btn');

    function openConcernEditModal() {
        modalTitle.value = detail.currentTitle || '';
        modalContent.value = detail.currentContent || '';
        editModal.classList.add('active');
        modalTitle.focus();
    }

    function closeConcernEditModal() {
        editModal.classList.remove('active');
    }

    if (concernEditBtn) {
        concernEditBtn.addEventListener('click', openConcernEditModal);
    }

    if (modalCancelBtn) {
        modalCancelBtn.addEventListener('click', closeConcernEditModal);
    }

    if (editModal) {
        editModal.addEventListener('click', (e) => {
            if (e.target === editModal) closeConcernEditModal();
        });
    }

    if (modalConfirmBtn) {
        modalConfirmBtn.addEventListener('click', async () => {
            const title = modalTitle.value.trim();
            const content = modalContent.value.trim();

            if (!title || !content) {
                alert('제목과 내용을 모두 입력해주세요.');
                return;
            }

            const res = await postForm(`/concern/${concernId}/update`, {
                concernTitle: title,
                concernContent: content
            });

            if (res.ok) {
                location.href = `/concern/${concernId}`;
            } else {
                alert('고민 수정에 실패했습니다.');
            }
        });
    }

    const concernDeleteBtn = document.getElementById('concern-delete-btn');

    if (concernDeleteBtn) {
        concernDeleteBtn.addEventListener('click', async () => {
            if (!confirm('고민을 삭제하시겠습니까? 복구할 수 없습니다.')) return;

            const res = await postForm(`/concern/${concernId}/remove`);

            if (res.ok) {
                location.href = '/concern';
            } else {
                alert('고민 삭제에 실패했습니다.');
            }
        });
    }

    document.querySelectorAll('.comment-like-btn').forEach(btn => {
        if (btn.disabled) return;

        btn.addEventListener('click', async () => {
            const commentId = btn.dataset.commentId;
            const res = await fetch(`/comment/${commentId}/like`, { method: 'POST' });
            if (!res.ok) {
                alert('좋아요 처리에 실패했습니다.');
                return;
            }

            const liked = btn.classList.toggle('liked');
            btn.querySelector('.comment-like-icon').textContent = liked ? '♥' : '♡';
            const countEl = btn.querySelector('.comment-like-count');
            countEl.textContent = liked
                ? Number(countEl.textContent) + 1
                : Number(countEl.textContent) - 1;
        });
    });

    const commentForm = document.querySelector('.comment-form');
    if (commentForm) {
        commentForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const textarea = commentForm.querySelector('textarea[name="commentContent"]');

            const res = await postForm(`/comment/${concernId}`, {
                commentContent: textarea.value
            });

            if (res.ok) {
                location.reload();
            } else {
                alert('댓글 작성에 실패했습니다.');
            }
        });
    }

    document.querySelectorAll('.delete-comment-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            if (!confirm('댓글을 삭제하시겠습니까?')) return;

            const commentId = btn.dataset.commentId;
            const res = await deleteRequest(`/comment/${commentId}`);

            if (res.ok) {
                btn.closest('.comment-item').remove();
            } else {
                alert('댓글 삭제에 실패했습니다.');
            }
        });
    });

    document.querySelectorAll('.edit-comment-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const commentId = btn.dataset.commentId;
            const commentItem = btn.closest('.comment-item');
            const commentText = commentItem.querySelector('.comment-text');

            if (commentItem.querySelector('.edit-textarea')) return;

            const textarea = document.createElement('textarea');
            textarea.className = 'edit-textarea comment-textarea';
            textarea.value = commentText.textContent;

            const confirmBtn = document.createElement('button');
            confirmBtn.textContent = '확인';
            confirmBtn.className = 'comment-submit-btn';

            const cancelBtn = document.createElement('button');
            cancelBtn.textContent = '취소';
            cancelBtn.className = 'edit-cancel-btn';

            commentText.style.display = 'none';
            btn.style.display = 'none';
            commentItem.append(textarea, confirmBtn, cancelBtn);

            cancelBtn.addEventListener('click', () => {
                textarea.remove();
                confirmBtn.remove();
                cancelBtn.remove();
                commentText.style.display = '';
                btn.style.display = '';
            });

            confirmBtn.addEventListener('click', async () => {
                const res = await patchForm(`/comment/${commentId}`, {
                    commentContent: textarea.value
                });

                if (res.ok) {
                    commentText.textContent = textarea.value;
                    textarea.remove();
                    confirmBtn.remove();
                    cancelBtn.remove();
                    commentText.style.display = '';
                    btn.style.display = '';
                } else {
                    alert('댓글 수정에 실패했습니다.');
                }
            });
        });
    });
});

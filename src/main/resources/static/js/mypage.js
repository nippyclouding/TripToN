(function () {
    const panels = {
        concerns: {
            page: 0,
            endpoint: '/myPage/api/concerns',
            listId: 'concerns-list',
            paginationId: 'concerns-pagination',
            emptyText: '작성한 고민이 없습니다',
            renderItem: renderConcern
        },
        comments: {
            page: 0,
            endpoint: '/myPage/api/comments',
            listId: 'comments-list',
            paginationId: 'comments-pagination',
            emptyText: '작성한 댓글이 없습니다',
            renderItem: renderComment
        },
        concernLikes: {
            page: 0,
            endpoint: '/myPage/api/concern-likes',
            listId: 'concern-likes-list',
            paginationId: 'concern-likes-pagination',
            emptyText: '좋아요한 고민이 없습니다',
            renderItem: renderConcernLike
        },
        commentLikes: {
            page: 0,
            endpoint: '/myPage/api/comment-likes',
            listId: 'comment-likes-list',
            paginationId: 'comment-likes-pagination',
            emptyText: '좋아요한 댓글이 없습니다',
            renderItem: renderCommentLike
        }
    };

    document.addEventListener('DOMContentLoaded', () => {
        loadAll();
    });

    async function loadAll() {
        await Promise.all([
            loadProfile(),
            loadPanel(panels.concerns),
            loadPanel(panels.comments),
            loadPanel(panels.concernLikes),
            loadPanel(panels.commentLikes)
        ]);
    }

    async function loadProfile() {
        try {
            const res = await fetch('/myPage/api/profile');
            if (res.status === 401) {
                window.location.href = '/';
                return;
            }
            if (!res.ok) throw new Error('profile load failed');

            const profile = await res.json();
            setText('mypage-nickname', profile.memberNickName ?? '');
            setText('mypage-email', profile.memberEmail ?? '');
        } catch (error) {
            setText('mypage-nickname', '-');
            setText('mypage-email', '-');
        }
    }

    async function loadPanel(panel) {
        const listEl = document.getElementById(panel.listId);
        const paginationEl = document.getElementById(panel.paginationId);
        if (!listEl || !paginationEl) return;

        listEl.innerHTML = '<div class="mypage-empty">불러오는 중...</div>';
        paginationEl.innerHTML = '';

        try {
            const res = await fetch(`${panel.endpoint}?page=${panel.page}`);
            if (res.status === 401) {
                window.location.href = '/';
                return;
            }
            if (!res.ok) throw new Error('panel load failed');

            const page = await res.json();
            panel.page = page.number ?? panel.page;
            panel.totalPages = page.totalPages ?? 0;

            renderList(listEl, panel, page.content ?? []);
            renderPagination(paginationEl, panel);
        } catch (error) {
            listEl.innerHTML = '<div class="mypage-empty">데이터를 불러오지 못했습니다</div>';
            paginationEl.innerHTML = '';
        }
    }

    function renderList(listEl, panel, items) {
        if (!items.length) {
            listEl.innerHTML = `<div class="mypage-empty">${escapeHtml(panel.emptyText)}</div>`;
            return;
        }

        listEl.innerHTML = items.map(panel.renderItem).join('');
    }

    function renderPagination(paginationEl, panel) {
        if (!panel.totalPages || panel.totalPages <= 0) {
            paginationEl.innerHTML = '';
            return;
        }

        paginationEl.innerHTML = `
            <button type="button" class="page-btn" data-direction="prev" ${panel.page <= 0 ? 'disabled' : ''}>&#8592;</button>
            <span class="page-info">${panel.page + 1} / ${panel.totalPages}</span>
            <button type="button" class="page-btn" data-direction="next" ${panel.page + 1 >= panel.totalPages ? 'disabled' : ''}>&#8594;</button>
        `;

        paginationEl.querySelectorAll('.page-btn').forEach((button) => {
            button.addEventListener('click', async () => {
                if (button.dataset.direction === 'prev' && panel.page > 0) {
                    panel.page -= 1;
                    await loadPanel(panel);
                }

                if (button.dataset.direction === 'next' && panel.page + 1 < panel.totalPages) {
                    panel.page += 1;
                    await loadPanel(panel);
                }
            });
        });
    }

    function renderConcern(item) {
        return `
            <a href="/concern/${encodeURIComponent(item.concernId ?? '')}" class="mypage-item">
                <span class="item-title">${escapeHtml(item.concernTitle ?? '')}</span>
                <span class="item-meta">${formatDate(item.createdAt)}</span>
            </a>
        `;
    }

    function renderComment(item) {
        return `
            <div class="mypage-item">
                <span class="item-title">${escapeHtml(item.commentContent ?? '')}</span>
                <span class="item-meta">${formatDate(item.createdAt)}</span>
            </div>
        `;
    }

    function renderConcernLike(item) {
        return `
            <a href="/concern/${encodeURIComponent(item.concernId ?? '')}" class="mypage-item">
                <span class="item-title">${escapeHtml(item.concernTitle ?? '')}</span>
            </a>
        `;
    }

    function renderCommentLike(item) {
        return `
            <div class="mypage-item">
                <span class="item-title">${escapeHtml(item.commentContent ?? '')}</span>
            </div>
        `;
    }

    function formatDate(value) {
        if (!value) return '';
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) return '';

        const pad = (n) => String(n).padStart(2, '0');
        return `${String(date.getFullYear()).slice(2)}.${pad(date.getMonth() + 1)}.${pad(date.getDate())}`;
    }

    function setText(id, value) {
        const el = document.getElementById(id);
        if (el) el.textContent = String(value);
    }

    function escapeHtml(value) {
        return String(value)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#39;');
    }
})();

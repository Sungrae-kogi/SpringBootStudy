// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace('/articles');
            });
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    // 클릭 이벤트 감지시 수정 API 요청
    modifyButton.addEventListener('click', event => {
        /*
            location -> 현재 브라우저의 URL 정보를 제공하는 객체
            location.search -> 현재 URL에서 ? 뒤에 오는 쿼리 문자열을 반환합니다.
         */
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        /* 가져온 id 값의 정보에 수정 요청을 보냅니다.
           fetch(url, options)의 구성 fetch함수는 Promise 객체를 반환 ( 비동기 처리 )-> 요청 결과를 기다리지 않고 다음 작업을 진행.
           버튼 클릭시 서버로 요청 같은 기능 구현시 addEventListener와 사용할 수 있다.
        */
        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정이 완료되었습니다.');
                location.replace(`/articles/${id}`);
            });
    });
}

// 생성 기능
const createButton = document.getElementById("create-btn");
if (createButton) {
    createButton.addEventListener('click', (event) => {
        fetch(`/api/articles`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById("title").value,
                content: document.getElementById("content").value,
            }),
        }).then(() => {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        });
    });
}

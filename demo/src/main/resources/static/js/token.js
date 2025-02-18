const token = searchParam('token')
// Param으로 넘어온 토큰이 존재한다면 토큰을 로컬 스토레이지에 저장함

if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key){
    return new URLSearchParams(location.search).get(key);
}

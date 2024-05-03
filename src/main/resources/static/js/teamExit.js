document.addEventListener('DOMContentLoaded' ()=>{
    var exitLink = document.getElementById('exitLink');
    if(exitLink) {
        exitLink.addEventListener('click', (e) =>{
            e.preventDefault();
            if(confirm('정말로 팀을 탈퇴하시겠습니까?')) {
                window.location.href = window.location.href + "exit";
            }
        });
    }
});
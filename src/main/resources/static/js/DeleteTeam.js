function deleteTeam(element) {
    var confirmed = confirm('이 결정은 돌이킬 수 없습니다.\n 정말로 팀을 해산시키겠습니까?');

    if(confirmed) {
        var teamId = element.getAttribute('data-team-id');

        fetch(`/team/deleteTeam/${teamId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if(response.ok){
                alert('팀 해산 완료');
                window.location.href=`/main`;
            } else{
                alert('팀 해산 실패');
            }
        })
        .catch(error => console.error('error', error));
    }
}
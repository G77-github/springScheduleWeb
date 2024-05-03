function deleteEvent(element) {
    var eventId = element.getAttribute('data-event-id');
    var teamId = element.getAttribute('data-team-id');

    fetch(`/team/${teamId}/event?eventId=${eventId}`, {
        method: 'DELETE',
    })
    .then(response => {
        if(response.ok){
            alert('이벤트 삭제 완료');
            window.location.href=`/team/${teamId}`;
        } else{
            alert('이벤트 삭제 실패');
        }
    })
    .catch(error => console.error('error', error));
}
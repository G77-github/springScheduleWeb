function editEvent(element) {
    var teamId = element.getAttribute('data-team-id');
    var eventId = element.getAttribute('data-event-id');
    window.location.href = `/team/${teamId}/eventEdit?eventId=${eventId}`;
}
function updateUserParticipation(eventId, userId, userParticipate) {
    fetch('/update-participation', {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({eventId, userId, userParticipate}),
    })
    .then(response => response.json())
    .then(data => {
        console.log("change user participation success", data);
    })
    .catch((error)=>{
        console.log('change user participation error', error)
    });
}
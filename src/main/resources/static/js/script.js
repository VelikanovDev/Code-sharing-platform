function send() {
    console.log("here");
    let object = {
        "code": document.getElementById("snippet-input").value
    };

    console.log("here 2");

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/code/new', false);
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    if (xhr.status === 200) {
        alert("Success!");
        window.location.href = "/code/latest";
    }
}

function addNewSnippet() {
    window.location.href = "/code/new";
}

function logout() {
    window.location.href = "/logout";
}

function register() {
    window.location.href = "/register";
}

function login() {
    window.location.href = "/login";
}

function deleteSnippet(id) {
    const url = '/code/delete/' + id;

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                console.error('Failed to delete snippet');
            }
        })
        .catch(error => {
            console.error('Network error:', error);
        });
}

function deleteAllSnippets() {
    const url = '/code/delete'

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                console.error('Failed to delete snippets');
            }
        })
        .catch(error => {
            console.error('Network error:', error);
        });
}

function editSnippet(id) {
    window.location.href = "/code/edit/" + id;
}

function showUsers() {
    window.location.href = "/code/users";
}

function backToLatest() {
    window.location.href = "/code/latest";
}
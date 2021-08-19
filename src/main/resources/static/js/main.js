$(document).ready(function () {

    const url = 'http://localhost:8189/api/users';
    let output = '';

    const renderUsers = (users) => {
        users.forEach(user => {
            output += `
            <tr id="${user.id}">
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.username}</td>
                <td>${user.roles.map(role => role.name.replace('ROLE_', ''))}</td>
                <td id="${user.id}"><a class="btn btn-info text-white" id="edit-user">Edit</a></td>
                <td id="${user.id}"><a class="btn btn-danger text-white" id="delete-user">Delete</a></td>
            </tr>
        `;
            const usersList = document.querySelector('#usersTable > tbody');
            usersList.innerHTML = output;
        })
    };

    let dBtn = document.getElementById('dBtn');
    let eBtn = document.getElementById('eBtn');
    let aBtn = document.getElementById('aBtn');

    //all users
    fetch(url)
    .then(res => res.json())
    .then(data => renderUsers(data));

    document.getElementById('usersTable').addEventListener('click', (e) => {
        e.preventDefault();
        let deleteButtonIsPressed = e.target.id === 'delete-user';
        let editButtonIsPressed = e.target.id === 'edit-user';

        let id = e.target.parentElement.id;

        // delete user
        if (deleteButtonIsPressed) {
            fetch(`${url}/${id}`, {
                method: 'GET'
            })
            .then(res => res.json())
            .then(user => {
                document.getElementById('deleteId').value = `${user.id}`;
                document.getElementById('deleteFirstName').value = `${user.firstName}`;
                document.getElementById('deleteLastName').value = `${user.lastName}`;
                document.getElementById('deleteAge').value = `${user.age}`;
                document.getElementById('deleteEmail').value = `${user.username}`;
                document.getElementById('deletePassword').value = `${user.password}`;
            })
            $('#deleteModal').modal('show');
        }

        // edit modal
        if (editButtonIsPressed) {
            fetch(`${url}/${id}`, {
                method: 'GET'
            })

            .then(res => res.json())
            .then(user => {
                document.getElementById('formId').value = `${user.id}`;
                document.getElementById('formFirstName').value = `${user.firstName}`;
                document.getElementById('formLastName').value = `${user.lastName}`;
                document.getElementById('formAge').value = `${user.age}`;
                document.getElementById('formEmail').value = `${user.username}`;
                document.getElementById('formPassword').value = ``;
            })
            $('#exampleModal').modal('show');
        }
    })

    //edit button
    eBtn.addEventListener('click', () => {
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                id: document.getElementById('formId').value,
                firstName: document.getElementById('formFirstName').value,
                lastName: document.getElementById('formLastName').value,
                age: document.getElementById('formAge').value,
                username: document.getElementById('formEmail').value,
                password: document.getElementById('formPassword').value,
                roles: getRoles('#formRoles')
            })
        })
        .then($('#exampleModal').modal('hide'))
        .then(response => response.json())
        .then(data => replaceRow(data)
        )
    });

    //delete button
    dBtn.addEventListener('click', e => {
        let target = $('#deleteId').val()
        fetch(`${url}/${target}`, {
            method: 'DELETE'
        })
        .then($('#deleteModal').modal('hide'));
        document.getElementById(target).remove();
    });

    //add button
    aBtn.addEventListener('click', (e) => {
        e.preventDefault();
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({
                lastName: document.getElementById('NewlastName').value,
                firstName: document.getElementById('NewfirstName').value,
                age: document.getElementById('Newage').value,
                username: document.getElementById('Newusername').value,
                password: document.getElementById('Newpassword').value,
                roles: getRoles('#newUserRoles')
            })
        }).then(res => res.json())
        .then(data => addRowsFromData(data))
        $('#home-tab').tab('show')
    })

    function replaceRow(data) {
        $.each(data, function () {
            row = `<tr id="${data.id}">
                        <td>${data.id}</td>
                        <td>${data.firstName}</td>
                        <td>${data.lastName}</td>
                        <td>${data.age}</td>
                        <td>${data.username}</td>
                        <td>${data.roles.map(role => role.name.replace('ROLE_', ''))}</td>
                        <td id="${data.id}"><a class="btn btn-info text-white" id="edit-user">Edit</a></td>
                        <td id="${data.id}"><a class="btn btn-danger text-white" id="delete-user">Delete</a></td>
                    </tr>
            `;
        });
        document.getElementById(data.id).innerHTML = row;
    }

    function getRoles(adress) {
        var data = [];
        $(adress).find('option:selected').each(function () {
            data.push({id: $(this).val(), name: $(this).text(), authority: $(this).text()});
        });
        return data;
    }

    function addRowsFromData(data) {
        $.each(data, function () {
            row = '<tr id="' + data.id + '"><td>' + data.id + '</td><td>' + data.firstName + '</td>' + '<td>' + data.lastName + '</td>' +
                '<td>' + data.age + "</td>" + "<td>" + data.username + "</td>" + "<td>" + data.roles.map(
                    role => role.name.replace('ROLE_', '')) + '</td>' +
                '<td id="' + data.id + '"><a class="btn btn-info text-white" id="edit-user">Edit</a></td>' +
                '<td id="' + data.id + '"><a class="btn btn-danger text-white" id="delete-user">Delete</a></td>'
            ;
        });
        $('#usersTable').append(row);
    }

    $('.tabb').click(function () {
        $('.tabb').addClass('text-primary');
        $(this).removeClass('text-primary');
    });
})
;
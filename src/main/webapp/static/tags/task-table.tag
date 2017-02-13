<task-table>
    <button type="button" class="btn btn-default" onclick={ create }>Create</button>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>id</th>
            <th>title</th>
            <th>description</th>
            <th>status</th>
        </tr>
        </thead>
        <tbody>
        <tr each={ task in tasks }>
            <td>
                { task.id }
            </td>
            <td>
                { task.title }
            </td>
            <td>
                { task.description }
            </td>
            <td>
                { task.status }
            </td>
            <td>
                <!-- <button type="button" class="btn btn-default" >Edit</button> -->
                <!-- <button type="button" class="btn btn-success" show={ task.status == 'OPEN' } onclick={ parent.close }>Close</button> -->
                <!-- <button type="button" class="btn btn-warning" show={ task.status == 'OPEN' }>Cancel</button> -->
                <!-- <button type="button" class="btn btn-primary" show={ task.status != 'OPEN' }>Open</button> -->
                <button type="button" class="btn btn-danger" onclick={ parent.delete }>Delete</button>
            </td>
        </tr>
        </tbody>
    </table>
    <script>
        var self = this
        fetch('/api/tasks').then(function(response) {
            return response.json()
        }).then(function(json) {
            tasks = json
            self.update()
        })

        create(e) {
            data = {
                title: (Math.random()　* 10000000000000000).toString(),
                description: (Math.random()　* 10000000000000000).toString(),
                status: 'OPEN'
            }
            fetch('/api/tasks', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then(function(response) {
                return response.json()
            }).then(function(json) {
                tasks.push(json)
                self.update()
            })
        }

        close(e) {
            console.log(e)
        }

        delete(e) {
            var item = e.item
            fetch('/api/tasks/' + e.item.task.id, {
                method: 'DELETE'
            }).then(function() {
                index = tasks.indexOf(item.task)
                tasks.splice(index, 1)
                self.update()
            })
        }
     </script>
</task-table>

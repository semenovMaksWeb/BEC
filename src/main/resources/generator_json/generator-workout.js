const users_id = [59, 79, 25, 49];
const data = [];


for (var index_user = 0; index_user < users_id.length; index_user++) {
    for (let i = 1; i < 31; i++) {
        let day = i;
        if (i < 10) {
            day = `0${i}` 
        }
        data.push({
            id_user: users_id[index_user],
            date:`2022-12-${day} 00:00:00.000`
        })
    }
}
console.log(data);
const ids_ex = [];
const count_exercises = 36;
const count_workout = 121;
const min_exercises = 5;
// не больше чем count_exercises
const max_exercises = 10;
weight
const min_weight = 10;
const max_weight = 100;

for (let index = 1; index < count_workout; index++) {
    ids_ex.push(index);   
}

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function sample(array, size = 1) {
    const { floor, random } = Math;
    let sampleSet = new Set();
    for (let i = 0; i < size; i++) {
        let index;
        do { index = floor(random() * array.length); }
        while (sampleSet.has(index));
        sampleSet.add(index);
    }
    return [...sampleSet].map(i => array[i]);
}


const data = [];
for (var index = 1; index < count_workout; index++) {
    const count_row = getRandomInt(min_exercises, max_exercises);
    const ids_ex_un = sample(ids_ex, count_row);
    // console.log(ids_ex_un);
    for (var index_ex = 0; index_ex < ids_ex_un.length; index_ex++) {
        data.push({
            id_workourt: index,
            id_exercises:ids_ex_un[index_ex], 
            weight: getRandomInt(min_weight, max_weight)
        })
    };
}
console.log(data);
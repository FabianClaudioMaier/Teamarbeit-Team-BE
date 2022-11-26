let ctx   = canvas.getContext("2d");
function Cell(x,y){
    this.x = x;
    this.y = y;
}


var cells = [
    cell = new Cell(0,1),
    cell = new Cell(1,0),
    cell = new Cell(1,2),
    cell = new Cell(3,1)
];


console.log(cells);

//var cells = [];
//var grid = [];
var colums = 4;
var rows = colums;

//fillCells();
//fillGrid();   
//console.log(grid);       
setInterval(() => { 
    ctx.clearRect(0,0,256,256); 
    ctx.fillStyle = "white";
    cells.forEach(Cell => ctx.fillRect(cell.x * (256/rows), cell.y * (256/colums), 256/rows, 256/colums)); 
    console.log(cells);
    updateCells();
    console.log(cells);
}, 1205);



function fillCells(){
for (let i = 0; i < 6; i++) {
var x = Math.floor(Math.random()*colums);
var y = Math.floor(Math.random()*rows);
if(cells.filter(cell => cell.x == x && cell.y == y) == 0){ cells.push(new Cell(x,y))};
};
}

function updateCells(){
var save = cells;
for (let x = 0; x < colums; x++) {
    for (let y = 0; y < rows; y++) {
        var sum = 0;
        //look for neighborgs
        for (let i = (x-1 < 0? 0 : x-1); i <= x+1 && i < colums; i++) {
            for (let j = (y-1 < 0? 0 : y-1); j <= y+1 && j < rows; j++) {
                if(save.filter(cell => cell.x == i && cell.y == j && !(cell.x == x && cell.y == y)) != 0 ){ sum++; }       
            };   
        };
        //--sum; to exclude the block itself which was include in the for-loops
        var eins = 1;

        var cellExists = save.filter(cell => cell.x == x && cell.y == y).length;

        if((cellExists == 0 || cellExists == undefined) &&  sum == 3){
            cells.push(new Cell(x,y)); //if the [x,y] pair has 3 neighbours and is not alife => get alife
        };

        if(cellExists != undefined && (sum <= 1 || sum >= 4)){
            
            var deleteIndex = cells.findIndex(checkCells);

            function checkCells(cell) {
                return cell.x == x && cell.y == y;
            }
            
            cells.splice(deleteIndex, 1) //if the [x,y] pair is alife and has to few or to many neighbours => die
        };
    };
}
}

let ctx   = canvas.getContext("2d");
var cells = [[1,0],[0,1],[1,2],[3,1]];
//var cells = [];
var grid = [];
var colums = 4;
var rows = colums;

//fillCells();
//fillGrid();   
//console.log(grid);       
setInterval(() => { 
    ctx.clearRect(0,0,256,256); 
    ctx.fillStyle = "white";
    cells.forEach(([x,y]) => ctx.fillRect(x * (256/rows), y * (256/colums), 256/rows, 256/colums)); 
    console.log(cells);
    updateCells();
    console.log(cells);
}, 1205);



function fillCells(){
for (let i = 0; i < 6; i++) {
var x = Math.floor(Math.random()*colums);
var y = Math.floor(Math.random()*rows);
if(cells.filter(([i,j]) => i == x && j == y).length == 0){ cells.push([x,y])};
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
                if(save.filter(([x,y]) => x == i && y == j).length != 0 && !(i == x && j == y)){ sum++; }       
            };   
        };
        //--sum; to exclude the block itself which was include in the for-loops
        var eins = 1;

        var cellExists = save.filter(([i,j]) => i == x && j == y).length;

        if((cellExists == 0 || cellExists == undefined) &&  sum == 3){
            cells.push([x,y]); //if the [x,y] pair has 3 neighbours and is not alife => get alife
        };

        if(cellExists != undefined && (sum <= 1 || sum >= 4)){
            
            var deleteIndex = cells.findIndex(checkCells);

            function checkCells([i,j]) {
                return i == x && j == y;
            }
            
            cells.splice(0, 1) //if the [x,y] pair is alife and has to few or to many neighbours => die
        };
    };
}
}

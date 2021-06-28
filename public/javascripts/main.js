function createNetwork(edges,attr) {
    // create an array with nodes
//    var nodes = new vis.DataSet([
//      { id: 1, label: "Node 1" },
//      { id: 2, label: "Node 2" },
//      { id: 3, label: "Node 3" },
//      { id: 4, label: "Node 4" },
//      { id: 5, label: "Node 5" },
//    ]);
//
//    // create an array with edges
//    var edges = new vis.DataSet([
//      { from: 1, to: 1 },
//      { from: 1, to: 2 },
//      { from: 3, to: 4 },
//      { from: 4, to: 5 },
//      { from: 3, to: 3 },
//    ]);
    console.log(edges);
    console.log(attr);
    var nodes = new vis.DataSet(attr);

    // create an array with edges
    var edges1 = new vis.DataSet(edges);

    // create a network
    var container = document.getElementById("mynetwork1");
    var data = {
      nodes: nodes,
      edges: edges1,
    };
    var options = {};
    var network = new vis.Network(container, data, options);
}
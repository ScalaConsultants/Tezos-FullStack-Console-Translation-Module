let nearley = require("nearley");
let Michelson = require("./Michelson");

function preProcessMichelsonScript(code) {
    let sections = new Map();
    sections['parameter'] = code.search(/parameter/),
        sections['storage'] = code.search(/storage/),
        sections['code'] = code.search(/code/)

    const boundaries = Object.values(sections).sort((a, b) => Number(a) - Number(b));
    sections[Object.keys(sections).find(key => sections[key] === boundaries[0]) + ''] = code.substring(boundaries[0], boundaries[1]);
    sections[Object.keys(sections).find(key => sections[key] === boundaries[1]) + ''] = code.substring(boundaries[1], boundaries[2]);
    sections[Object.keys(sections).find(key => sections[key] === boundaries[2]) + ''] = code.substring(boundaries[2]);

    const parts = [sections['parameter'], sections['storage'], sections['code']];

    return parts.map(p => p.trim().split('\n').map(l => l.replace(/\#[\s\S]+$/, '').trim()).filter(v => v.length > 0).join(' '));
}

function translateMichelsonToMicheline(code) {
    const parser = new nearley.Parser(nearley.Grammar.fromCompiled(Michelson));
    preProcessMichelsonScript(code).forEach(p => {
        parser.feed(p);
});

    return parser.results[0];
}

let input = process.argv[2];

let result = translateMichelsonToMicheline(input);

let wrappedMicheline = ` { "script": ${result} } `;

console.log(wrappedMicheline);

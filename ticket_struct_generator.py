# ticket_struct_generator.py

ticket_system = {
    "User": {
        "id": "Long",
        "username": "String",
        "password": "String",
        "role": "String"
    },
    "Ticket": {
        "id": "Long",
        "description": "String",
        "status": "String",
        "createdAt": "LocalDateTime",
        "createdBy": "User",
        "assignedTo": "User"
    }
}

def create_entity(name, fields):
    lines = [f"@Entity\npublic class {name} {{"]
    for field, type_ in fields.items():
        if field == "id":
            lines.append("    @Id\n    @GeneratedValue")
        elif type_ == "User":
            lines.append("    @ManyToOne")
        lines.append(f"    private {type_} {field};")
    lines.append("}")
    return "\n".join(lines)

for class_name, fields in ticket_system.items():
    java_class = create_entity(class_name, fields)
    with open(f"{class_name}.java", "w") as f:
        f.write(java_class)
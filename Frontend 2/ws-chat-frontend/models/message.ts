export default interface MessageDto{
    name: string, //destination name of group/user
    isGroup: boolean, //flag if destinationName fits to a group
    message: string, //content of the message
    timestamp: Date, 
    username: string //username sender of the message 
}
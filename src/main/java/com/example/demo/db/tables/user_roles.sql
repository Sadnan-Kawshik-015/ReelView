CREATE TABLE [dbo].[user_roles] (
    [user_id] VARCHAR (128) NOT NULL,
    [role_id] VARCHAR (128) NOT NULL,
    CONSTRAINT [PK__user_rol__6EDEA1535E49783D] PRIMARY KEY CLUSTERED ([user_id] ASC, [role_id] ASC),
    CONSTRAINT [FKh8ciramu9cc9q3qcqiv4ue8a6] FOREIGN KEY ([role_id]) REFERENCES [dbo].[roles] ([id]),
    CONSTRAINT [FKhfh9dx7w3ubf1co1vdev94g3f] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([id])
    );


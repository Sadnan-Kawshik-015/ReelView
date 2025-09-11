CREATE TABLE [dbo].[users] (
    [id]                             VARCHAR (128)  NOT NULL,
    [email]                          VARCHAR (128)  NOT NULL,
    [first_name]                     VARCHAR (128)  NOT NULL,
    [last_name]                      VARCHAR (128)  NULL,
    [password]                       VARCHAR (128)  NOT NULL,
    [mobile_number]                  VARCHAR (16)   NOT NULL,
    [password_reset_token]           VARCHAR (128)  NULL,
    [password_reset_token_expire_on] DATETIME       NULL,
    [failed_login_attempt]           INT            NOT NULL,
    [is_locked]                      BIT            NOT NULL,
    [otp]                            VARCHAR (8)    NULL,
    [otp_expire_on]                  DATETIME       NULL,
    [is_active]                      BIT            NOT NULL,
    [is_email_verified] BIT NOT NULL DEFAULT 0,
    [email_verification_token] VARCHAR(128) NULL,
    [email_verification_token_expire_on] DATETIME NULL,
    [is_mobile_verified] BIT NOT NULL DEFAULT 0,
    [mobile_verification_token] VARCHAR(128) NULL,
    [mobile_verification_token_expire_on] DATETIME NULL,
    [password_changed_on] DATETIME NULL,
    CONSTRAINT [PK__users__3213E83FAE05FFB0] PRIMARY KEY CLUSTERED ([id] ASC),
    CONSTRAINT [UK6dotkott2kjsp8vw4d0m25fb7] UNIQUE NONCLUSTERED ([email] ASC),
    );


GO

CREATE INDEX [IX_users_name] ON [dbo].[users] ([first_name],[last_name])

    GO

